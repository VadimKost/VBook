package com.example.vbook.device.di

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.example.vbook.device.service.mediaservice.ForegroundBookPlayerServiceProxy
import com.example.vbook.domain.bookplayer.BookPlayer
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//note for my self. It must be service scoped becose of player release on destroy
@Module
@InstallIn(ServiceComponent::class)
class MediaPlayerModule {

    @Provides
    @ServiceScoped
    fun provideAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_SPEECH)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @ServiceScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ) = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .build()

    @Provides
    @ServiceScoped
    fun provideMediaSession(
        @ApplicationContext context: Context
    ) = MediaSessionCompat(context, "VBookService")


}

@Module
@InstallIn(SingletonComponent::class)
class SUKA {
    @Provides
    @Singleton
    fun provideMediaPlayer(@ApplicationContext context: Context): BookPlayer{
        return ForegroundBookPlayerServiceProxy(context)
    }
}
