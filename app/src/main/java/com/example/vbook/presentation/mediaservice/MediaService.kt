package com.example.vbook.presentation.mediaservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.PlaybackStateCompat

import android.support.v4.media.MediaMetadataCompat

import com.google.android.exoplayer2.SimpleExoPlayer

import android.support.v4.media.session.MediaSessionCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.app.PendingIntent
import com.example.vbook.domain.model.Book

import com.example.vbook.presentation.MainActivity
import com.google.android.exoplayer2.MediaItem
import android.os.Binder
import androidx.media.session.MediaButtonReceiver
import android.app.Notification
import android.net.Uri
import android.util.Log
import android.widget.Toast

import androidx.core.content.ContextCompat

import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import com.example.vbook.R
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.usecases.GetCurrentBook
import com.example.vbook.domain.usecases.UpdateBook
import kotlinx.coroutines.*

@AndroidEntryPoint
class MediaService: Service() {
    private val NOTIFICATION_ID = 404
    private val NOTIFICATION_DEFAULT_CHANNEL_ID = "default_channel"

    @Inject
    lateinit var getCurrentBook: GetCurrentBook
    @Inject
    lateinit var updateBook: UpdateBook

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    lateinit var currentBook: Book

    private val metadataBuilder = MediaMetadataCompat.Builder()

    private val stateBuilder = PlaybackStateCompat.Builder().setActions(
        PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    )
    var mediaSession: MediaSessionCompat? = null

    fun updateMetadataFromBook(book: Book){
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ART_URI, book.bookURL)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, book.title)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, book.cycle.first)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, book.author.first)
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 11)
        mediaSession!!.setMetadata(metadataBuilder.build())
    }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession!!.setCallback(mediaSessionCallback)
        val appContext = applicationContext
        val activityIntent = Intent(appContext, MainActivity::class.java)
        mediaSession!!.setSessionActivity(
            PendingIntent.getActivity(appContext, 0, activityIntent, 0)
        )

        val mediaButtonIntent = Intent(
            Intent.ACTION_MEDIA_BUTTON, null, appContext,
            MediaButtonReceiver::class.java
        )
        mediaSession!!.setMediaButtonReceiver(
            PendingIntent.getBroadcast(
                appContext,
                0,
                mediaButtonIntent,
                0
            )
        )
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Ресурсы освобождать обязательно
        mediaSession!!.release()
    }

    val mediaSessionCallback=object :MediaSessionCompat.Callback(){
        var currentState = PlaybackStateCompat.STATE_STOPPED
        override fun onPlay() {
            GlobalScope.launch{
                val book = getCurrentBook()
                if (book is Resource.Success){
                    currentBook=book.data
                    updateMetadataFromBook(currentBook)

                    mediaSession!!.setActive(true);

                    // Сообщаем новое состояние
                    mediaSession!!.setPlaybackState(
                        stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                            PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1F).build());

                    // Загружаем URL аудио-файла в ExoPlayer
                    val url= currentBook.mp3List?.get(currentBook.stoppedTrackIndex)?.second
                    Log.e("VVV",url!!)
                    val mediaItem = MediaItem.fromUri(url!!)
                    withContext(Dispatchers.Main){
                        exoPlayer.setMediaItem(mediaItem)
                        exoPlayer.prepare()

                        exoPlayer.seekTo(currentBook.stoppedTrackTime)
                        // Запускаем воспроизведение
                        exoPlayer.setPlayWhenReady(true);
                    }

                    mediaSession!!.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1F).build());
                    currentState = PlaybackStateCompat.STATE_PLAYING;

                    refreshNotificationAndForegroundStatus(currentState);
                }else if (book is Resource.Error){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MediaService, book.message, Toast.LENGTH_SHORT).show()
                    }

                    }

                }

            }
        override fun onPause() {
            if (exoPlayer.getPlayWhenReady()) {
                exoPlayer.setPlayWhenReady(false);
                currentBook.stoppedTrackTime=exoPlayer.currentPosition
                runBlocking {
                    updateBook.invoke(currentBook)
                }
            }
            mediaSession!!.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1F).build());
            currentState = PlaybackStateCompat.STATE_PAUSED;

            refreshNotificationAndForegroundStatus(currentState);
        }
        }




    inner class PlayerServiceBinder : Binder() {
        val mediaSessionToken: MediaSessionCompat.Token = mediaSession!!.sessionToken
    }
    override fun onBind(intent: Intent): IBinder {
        return PlayerServiceBinder()
    }

    private fun refreshNotificationAndForegroundStatus(playbackState: Int) {
        when (playbackState) {
            PlaybackStateCompat.STATE_PLAYING -> {
                startForeground(NOTIFICATION_ID, getNotification(playbackState))
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                NotificationManagerCompat.from(this)
                    .notify(NOTIFICATION_ID, getNotification(playbackState))
                stopForeground(false)
            }
            else -> {
                stopForeground(true)
            }
        }
    }

    private fun getNotification(playbackState: Int): Notification {
        val builder: NotificationCompat.Builder = MediaStyleHelper.from(this, mediaSession!!)
        builder.addAction(
            NotificationCompat.Action(
                android.R.drawable.ic_media_previous,
                getString(R.string.previ),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
        )
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) builder.addAction(
            NotificationCompat.Action(
                R.drawable.exo_icon_pause,
                getString(R.string.pause),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PAUSE
                )
            )
        ) else builder.addAction(
            NotificationCompat.Action(
                R.drawable.exo_icon_play,
                getString(R.string.play),
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    this,
                    PlaybackStateCompat.ACTION_PLAY
                )
            )
        )
        builder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        this,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
                .setMediaSession(mediaSession!!.sessionToken)
        ) // setMediaSession требуется для Android Wear
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setColor(
            ContextCompat.getColor(
                this,
                R.color.purple_500
            )
        ) // The whole background (in MediaStyle), not just icon background
        builder.setShowWhen(false)
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setOnlyAlertOnce(true)
        builder.setChannelId(NOTIFICATION_DEFAULT_CHANNEL_ID)
        return builder.build()
    }
}