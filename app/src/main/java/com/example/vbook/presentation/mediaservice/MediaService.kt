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
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetCurrentBook
import com.example.vbook.domain.usecases.UpdateBook
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MediaService: Service() {
    lateinit var notificationManager: NotificationManager
    private var currentBook: Book? = null
    private var isForegroundService = false
    private val NOTIFICATION_ID = 404
    private val NOTIFICATION_DEFAULT_CHANNEL_ID = "default_channel"

    @Inject
    lateinit var getCurrentBook: GetCurrentBook
    @Inject
    lateinit var updateBook: UpdateBook

    @Inject
    lateinit var player: SimpleExoPlayer
    
    private lateinit var mediaSessionConnector: MediaSessionConnector
    lateinit var mediaSession: MediaSessionCompat

    suspend fun updateCurrentBook(){
        val book=getCurrentBook()
        currentBook= if (book is Resource.Success) book.data else null
        preparePlayList(currentBook!!)
    }
    override fun onCreate() {
        super.onCreate()
        runBlocking {
            updateCurrentBook()
        }
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
            player.addMediaItem(MediaItem.Builder().setUri(track.second).setMediaMetadata(getMediaDataFromBook(index)).build())
        }

        player.prepare()
        player.seekTo(initialWindowIndex, playbackStartPositionMs)
    }
    fun getMediaDataFromBook(trackNumber: Int):MediaMetadata{
        return MediaMetadata.Builder()
            .setTitle(currentBook?.title)
            .setTrackNumber(trackNumber)
            .build()
    }



    inner class PlayerServiceBinder : Binder() {
        val mediaSessionToken: MediaSessionCompat.Token = mediaSession.sessionToken
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
                    currentBook!!.stoppedTrackIndex= player.currentWindowIndex
                    currentBook!!.stoppedTrackTime= player.contentPosition
                    updateBook(currentBook!!)
                }
            }else{
                Log.e("v","play")
                runBlocking { updateCurrentBook()}
                notificationManager.showNotificationForPlayer(player)
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



