package com.example.vbook.presentation.mediaservice

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import com.example.vbook.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class NotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    playerNotificationListener:  PlayerNotificationManager.NotificationListener,
) {
    val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        notificationManager = PlayerNotificationManager.Builder(
            context,1,"12",DescriptionAdapter(mediaController)
        )
            .setNotificationListener(playerNotificationListener)
            .build()
//        notificationManager.setControlDispatcher(DefaultControlDispatcher())
        notificationManager.setMediaSessionToken(sessionToken)
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player){
        notificationManager.setPlayer(player)
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
        ): Bitmap? = context.getDrawable(R.drawable.ic_launcher_background)!!.toBitmap(100,100)

    }
}