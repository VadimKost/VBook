package com.example.vbook.presentation.service.mediaservice

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
import com.example.vbook.domain.usecases.UpdateBookUseCase
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
    lateinit var updateBookUseCase: UpdateBookUseCase


    private val _serviceBook = MutableStateFlow<Book?>(null)
    val serviceBook = _serviceBook.asStateFlow()

    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    lateinit var playbackInfo:Flow<MediaPlayerManager.PlaybackInfo>

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
            serviceCoroutineScope,
            updateBookUseCase::invoke,
            PlayerEventListener()
        )
        playbackInfo = mediaPlayerManager.playbackInfo
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("VVV", "onDestroy")
        mediaPlayerManager.saveBookStoppedIndexAndTime(_serviceBook.value)
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

    fun setBook(book: Book) {
        val oldBook = _serviceBook.value
        if (oldBook != book) {
            mediaPlayerManager.preparePlayListForPlayer(book,oldBook)
            _serviceBook.value = book
            mediaNotificationManager.showNotificationForPlayer(player)

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
                mediaPlayerManager.saveBookStoppedIndexAndTime(_serviceBook.value,)
            } else {
                mediaPlayerManager.updatePlayListStateInfo()
            }
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



