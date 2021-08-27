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
import com.example.vbook.getStateData
import com.example.vbook.presentation.common.UiState
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED
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

    private val _bookState = MutableStateFlow<UiState<Book>>(UiState.Loading)
    val booksState =_bookState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _hasNext = MutableStateFlow(false)
    val hasNext = _hasNext.asStateFlow()

    val trackIndex= MutableStateFlow(0)

    val trackTime = flow{
        while (true){
            emit(player.currentPosition)
            delay(1000L)
        }
    }

    private lateinit var notificationManager: NotificationManager
    private var isForegroundService = false

    @Inject
    lateinit var updateBook: UpdateBook
    
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    fun setBook(book: Book){
        val stateBook = getStateData(_bookState.value)
        if (stateBook != book){
            preparePlayList(book)
            _bookState.value =UiState.Success(book)
        }

    }
    fun onError(message:String){
        _bookState.value = UiState.Error(message)
    }

    override fun onCreate() {
        super.onCreate()
        player.prepare()
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
        savePlayListMeta(getStateData(_bookState.value),player.currentWindowIndex,player.contentPosition)
        mediaSession.release()
        player.release()
        scope.cancel()
    }

    private fun preparePlayList(book: Book) {
        val old =getStateData(_bookState.value)
        if (old != null){
            savePlayListMeta(old,player.currentWindowIndex,player.contentPosition)
            Log.d("BookProbable","${old.title} i= ${player.currentWindowIndex} t = ${player.contentPosition}")
        }

        player.clearMediaItems()
        val initialWindowIndex = book.stoppedTrackIndex
        val playbackStartPositionMs = book.stoppedTrackTime

        book.mp3List!!.forEachIndexed { index, track ->
            player.addMediaItem(MediaItem.Builder()
                .setUri(track.second)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(book.title)
                        .setArtist(book.author.first)
                        .setTrackNumber(index)
                        .build()
                )
                .build()
            )
        }
        player.seekTo(initialWindowIndex, playbackStartPositionMs)
        updatePlayBackMeta()
    }

    inner class PlayerServiceBinder : Binder() {
        val service = this@MediaService
    }
    override fun onBind(intent: Intent): IBinder {
        return PlayerServiceBinder()
    }
    private inner class PlayerEventListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updatePlayBackMeta()
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (!playWhenReady){
                stopForeground(false)
                isForegroundService = false
                updatePlayBackMeta()
                Log.d("Book","stop")
                savePlayListMeta(getStateData(_bookState.value),player.currentWindowIndex,player.contentPosition)
            }else{
                updatePlayBackMeta()
            }
        }
    }
    fun savePlayListMeta(book: Book?,trackIndex:Int,trackTime: Long){
        scope.launch {
            if (book != null){
                withContext(Main){
                    Log.d("BookSet","${book.title} i= $trackIndex t = ${player.contentPosition}")
                    book.stoppedTrackIndex= trackIndex
                    book.stoppedTrackTime= trackTime
                }
                withContext(IO){
                    updateBook(book)
                }
            }
        }

    }

    fun updatePlayBackMeta(){
        trackIndex.value = player.currentWindowIndex
        _isPlaying.value = player.playWhenReady
        _hasNext.value = player.hasNextWindow()
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



