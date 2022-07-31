package com.example.vbook.device.service.mediaservice

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
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.player.PlaybackInfo
import com.example.vbook.domain.usecase.book.SavePlaybackPositionUseCase
import com.example.vbook.domain.usecase.combination.GetAppropriateUriUseCase
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ServiceComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import java.lang.ref.WeakReference
import java.text.FieldPosition
import javax.inject.Inject

//MediaService is started and bound. It starts when setBook() is called
@AndroidEntryPoint
class MediaService : Service() {
    private val serviceCoroutineScope = CoroutineScope(IO)

    @Inject lateinit var player: ExoPlayer
    @Inject lateinit var getAppropriateUriUseCase: GetAppropriateUriUseCase
    @Inject lateinit var savePlayBackPositionUseCase: SavePlaybackPositionUseCase

    lateinit var mediaNotificationManager: MediaNotificationManager
    lateinit var mediaPlayerManager: MediaPlayerManager

    private var isForegroundService = false



    private val _serviceBook = MutableStateFlow<ResourceState<Book>>(ResourceState.Empty)
    val serviceBook = _serviceBook.asStateFlow()

    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var mediaSession: MediaSessionCompat

    lateinit var playbackInfo: StateFlow<ResourceState<PlaybackInfo>>

    override fun onCreate() {
        Log.e("Service", "onCreate")
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
            getAppropriateUriUseCase,
            serviceCoroutineScope
        )
        playbackInfo = mediaPlayerManager.playbackInfo
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Service", "onDestroy")
        val book = serviceBook.value.getOrNull()
        savePlaybackPositionOfBook(
            book,
            player.currentMediaItemIndex,
            player.currentPosition
        )
        mediaSession.release()
        player.release()
        serviceCoroutineScope.cancel()
    }

    class PlayerServiceBinder(service: MediaService) : Binder() {
        val service = WeakReference(service)
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("Service", "onBind")
        return PlayerServiceBinder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("Service", "onUnbind")
        return super.onUnbind(intent)
    }

    suspend fun setBook(book: Book): Boolean {
        val oldBook = serviceBook.value.getOrNull()
        return if (oldBook != book) {
            savePlaybackPositionOfBook(oldBook, player.currentMediaItemIndex, player.currentPosition)
            mediaPlayerManager.preparePlayListForPlayer(book)
            _serviceBook.value = ResourceState.Success(book)
            mediaNotificationManager.showNotificationForPlayer(player)
            true
        } else {
            false
        }
    }

    suspend fun replaceMediaItemUriSource(
        book: Book,
        index: Int,
        newUri: String
    ):Boolean {
        return mediaPlayerManager.replaceMediaItem(book, index, newUri)
    }

    private fun savePlaybackPositionOfBook(book: Book?, trackIndex: Int, position: Long) {
        serviceCoroutineScope.launch {
            if (book != null) {
                withContext(Dispatchers.Main) {
                    Log.d(
                        "BookSet",
                        "${book.title} i= $trackIndex t = $position"
                    )
                    book.stoppedTrackIndex = trackIndex
                    book.stoppedTrackTime = position
                }
                withContext(Dispatchers.IO) {
                    //TODO make usecase
                    savePlayBackPositionUseCase(book)
                }
            }
        }
    }


    private inner class PlayerEventListener : Player.Listener {

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaPlayerManager.updatePlaybackState()
        }

        override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
            val book = serviceBook.value.getOrNull()
            if (!playWhenReady) {
                stopForeground(false)
                isForegroundService = false
                mediaPlayerManager.updatePlaybackState()
                savePlaybackPositionOfBook(
                    book,
                    player.currentMediaItemIndex,
                    player.currentPosition
                )
            } else {
                mediaPlayerManager.updatePlaybackState()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e("BookError", error.toString())
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



