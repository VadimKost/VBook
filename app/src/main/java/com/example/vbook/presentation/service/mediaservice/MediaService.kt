package com.example.vbook.presentation.service.mediaservice

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vbook.common.model.Book
import com.example.vbook.data.repository.book.BookRepository
import com.example.vbook.data.repository.mediaitem.DownloadingItemRepository
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@AndroidEntryPoint
class MediaService : Service() {
    private val serviceCoroutineScope = CoroutineScope(IO)

    @Inject
    lateinit var player: ExoPlayer

    private lateinit var mediaNotificationManager: MediaNotificationManager
    private lateinit var mediaPlayerManager: MediaPlayerManager
    private var isForegroundService = false

    @Inject
    lateinit var bookRepository: BookRepository

    @Inject
    lateinit var downloadingItemRepository: DownloadingItemRepository


    private val _serviceBook = MutableStateFlow<Book?>(null)
    val serviceBook = _serviceBook.asStateFlow()

    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    lateinit var playbackInfo: Flow<MediaPlayerManager.PlaybackInfo>

    override fun onCreate() {
        Log.e("VVV", "onCreate")
        super.onCreate()
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

        mediaNotificationManager = MediaNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener(),
            serviceCoroutineScope
        )

        mediaPlayerManager = MediaPlayerManager(
            player,
            PlayerEventListener(),
            downloadingItemRepository
        )
        playbackInfo = mediaPlayerManager.playbackInfo
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("VVV", "onDestroy")
        saveBookStoppedIndexAndTime(_serviceBook.value)
        mediaSession.release()
        player.release()
        serviceCoroutineScope.cancel()
    }

    inner class PlayerServiceBinder : Binder() {
        val service = this@MediaService
    }

    override fun onBind(intent: Intent): IBinder {
        return PlayerServiceBinder()
    }

    suspend fun setBook(book: Book) {
        val oldBook = _serviceBook.value
        if (oldBook != book) {
            saveBookStoppedIndexAndTime(oldBook)
            mediaPlayerManager.preparePlayListForPlayer(book)
            _serviceBook.value = book
            mediaNotificationManager.showNotificationForPlayer(player)

        }

    }

    suspend fun replaceMediaItem(
        mediaUri: String,
        title: String,
        author: String,
        artworkUri: String,
        index: Int
    ) {
        mediaPlayerManager.replaceMediaItem(mediaUri, title, author, artworkUri, index)
    }

    private fun saveBookStoppedIndexAndTime(book: Book?) {
        serviceCoroutineScope.launch {
            if (book != null) {
                withContext(Dispatchers.Main) {
                    Log.d(
                        "BookSet",
                        "${book.title} i= ${player.currentMediaItemIndex} t = ${player.contentPosition}"
                    )
                    book.stoppedTrackIndex = player.currentMediaItemIndex
                    book.stoppedTrackTime = player.currentPosition
                }
                withContext(Dispatchers.IO) {
                    bookRepository.saveBookTimeLine(book)
                }
            }
        }
    }


    private inner class PlayerEventListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaPlayerManager.updatePlayListStateInfo()
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            if (!playWhenReady) {
                stopForeground(false)
                isForegroundService = false
                mediaPlayerManager.updatePlayListStateInfo()
                Log.d("Book", "stop")
                saveBookStoppedIndexAndTime(_serviceBook.value)
            } else {
                mediaPlayerManager.updatePlayListStateInfo()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e("BookError",error.toString())
            Toast.makeText(this@MediaService, "Something went wrong", Toast.LENGTH_SHORT).show()
            player.prepare()
            player.pause()
        }
    }

    private inner class PlayerNotificationListener :
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



