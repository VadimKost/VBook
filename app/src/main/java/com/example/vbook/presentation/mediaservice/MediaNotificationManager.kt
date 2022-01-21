package com.example.vbook.presentation.mediaservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.vbook.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MediaNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    playerNotificationListener: PlayerNotificationManager.NotificationListener,
    val coroutineScope: CoroutineScope
) {
    val notificationManager: PlayerNotificationManager
    var icon: Bitmap? = null

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        val notificationChannelId = createNotificationChannel("123", "MediaPlayer")
        notificationManager = PlayerNotificationManager.Builder(
            context,
            1,
            notificationChannelId
        )
            .setNotificationListener(playerNotificationListener)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .build()
//        notificationManager.setControlDispatcher(DefaultControlDispatcher())
        notificationManager.setMediaSessionToken(sessionToken)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        coroutineScope.launch(Dispatchers.Main) {
            val uri = player.currentMediaItem?.mediaMetadata?.artworkUri
            if (uri != null) {
                withContext(Dispatchers.IO){
                    icon = loadBookBitmap(context, uri)
                }
                notificationManager.setPlayer(player)
            }

        }
    }

    private suspend fun loadBookBitmap(context: Context, artworkUri: Uri): Bitmap {
        val loader = ImageLoader(context)
        return withContext(Dispatchers.Main) {
            val request = ImageRequest.Builder(context)
                .data(artworkUri)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()
            val result = (loader.execute(request).drawable)
            val bitmap = (result as BitmapDrawable).bitmap
            return@withContext bitmap
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH
            )
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)

            channelId
        } else {
            ""
        }
    }

    inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        override fun createCurrentContentIntent(player: Player): PendingIntent? =
            controller.sessionActivity

        override fun getCurrentContentText(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.artist.toString()
        }


        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title.toString()
        }


        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap {
            return icon ?: context.getDrawable(R.drawable.ic_launcher_background)!!
                .toBitmap(100, 100)
        }
    }

}
