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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MediaService: Service() {

    @Inject
    lateinit var player: SimpleExoPlayer

    private var _currentBook:MutableStateFlow<Book?> = MutableStateFlow(null)
    var currentBook = _currentBook.asStateFlow()

    val trackIndex= flow {
        while (true){
            val book=currentBook.value
            if (book != null){
                emit(book.stoppedTrackIndex)
            }
            delay(1000L)
        }
    }

    val trackTime = flow{
        while (true){
            val book=currentBook.value
            if (book != null){
                emit(book.stoppedTrackTime)
            }

            delay(1000L)
        }
    }

    private lateinit var notificationManager: NotificationManager
    private var isForegroundService = false

    @Inject
    lateinit var updateBook: UpdateBook
    
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    fun setCurrentBook(book: Book){
        if (book != currentBook.value){
            _currentBook.value=book
            preparePlayList(currentBook.value!!)
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
        Log.e("des","des")
        mediaSession.release()
        player.release()
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
            .setTitle(currentBook.value?.title)
            .setArtist(currentBook.value?.author?.first)
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

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (!playWhenReady){
                Log.e("v","stop")
                stopForeground(false)
                isForegroundService = false
                runBlocking{
                    currentBook.value!!.stoppedTrackIndex= player.currentWindowIndex
                    currentBook.value!!.stoppedTrackTime= player.contentPosition
                    updateBook(currentBook.value!!)
                    _currentBook.value = _currentBook.value?.copy()
                }
            }else{
                Log.e("v","play")
            }
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



