package com.example.vbook.data.mediaservice

import android.content.Context
import android.support.v4.media.session.PlaybackStateCompat

import androidx.media.session.MediaButtonReceiver

import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.vbook.R


object MediaStyleHelper {
    fun from(context: Context?, mediaSession: MediaSessionCompat): NotificationCompat.Builder {
        val controller = mediaSession.controller
        val mediaMetadata = controller.metadata
        val description = mediaMetadata.description
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!,"String");
        builder
            .setContentTitle(description.title)
            .setContentText(description.subtitle)
            .setSubText(description.description)
            .setLargeIcon(context!!.getDrawable(R.drawable.ic_launcher_background)?.toBitmap(40,40))
            .setContentIntent(controller.sessionActivity)
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        return builder
    }
}
