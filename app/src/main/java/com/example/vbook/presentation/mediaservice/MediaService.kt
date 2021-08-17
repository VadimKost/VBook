package com.example.vbook.presentation.mediaservice

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.UpdateBook
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@AndroidEntryPoint
class MediaService: Service() {
    val scope = CoroutineScope(IO)

    @Inject
    lateinit var player: SimpleExoPlayer

    var currentBook:Book? = null

    val trackIndex= flow {
        while (true){
            emit(player.currentWindowIndex)
            delay(1000L)
        }
    }

    val trackTime = flow{
        while (true){
            emit(player.currentPosition)
            delay(1000L)
        }
    }
    val _isPlaying= MutableStateFlow(false)
    val isPlaying= _isPlaying.asStateFlow()



    private lateinit var notificationManager: NotificationManager
    private var isForegroundService = false

    @Inject
    lateinit var updateBook: UpdateBook
    
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    fun makeBookCurrent(book: Book){
        if (book != currentBook){
            currentBook=book
            preparePlayList(currentBook!!)
        }
    }
    override fun onCreate() {
        super.onCreate()
        player.addListener(PlayerEventListener())
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        mediaSession = MediaSessionCompat(this, "VBookService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)

        notificationManager = NotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )
        notificationManager.showNotificationForPlayer(player)
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
        player.release()
        scope.cancel()
    }

    private fun preparePlayList(book: Book) {
        player.clearMediaItems()

        val initialWindowIndex = book.stoppedTrackIndex
        val playbackStartPositionMs=book.stoppedTrackTime
        Log.e("vas","i=${initialWindowIndex} and ms=${playbackStartPositionMs}")

        book.mp3List!!.forEachIndexed { index, track ->
            player.addMediaItem(MediaItem.Builder()
                .setUri(track.second)
                .setMediaMetadata(getMediaDataFromBook(index))
                .build()
            )
        }

        player.prepare()
        player.seekTo(initialWindowIndex, playbackStartPositionMs)
    }

    fun getMediaDataFromBook(trackNumber: Int):MediaMetadata{
        return MediaMetadata.Builder()
            .setTitle(currentBook?.title)
            .setArtist(currentBook?.author?.first)
            .setTrackNumber(trackNumber)
            .build()
    }



    inner class PlayerServiceBinder : Binder() {
        val service = this@MediaService
    }
    override fun onBind(intent: Intent): IBinder {
        return PlayerServiceBinder()
    }
    private inner class PlayerEventListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateBookPlayBackMetaData(isPlaying.value)
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (!playWhenReady){
                stopForeground(false)
                isForegroundService = false
                updateBookPlayBackMetaData(false)
            }else{
                updateBookPlayBackMetaData(true)
            }
        }
    }

    fun updateBookPlayBackMetaData(isPlaying:Boolean){
        scope.launch{
            withContext(Main){
                currentBook!!.stoppedTrackIndex= player.currentWindowIndex
                currentBook!!.stoppedTrackTime= player.contentPosition
            }
            _isPlaying.value = isPlaying
            updateBook(currentBook!!)
        }
    }

    inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@MediaService.javaClass)
                )
                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

}



