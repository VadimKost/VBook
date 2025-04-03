package com.example.vbook.device.di

import android.content.Context
import com.example.vbook.device.service.mediaservice.PlayerImpl
import com.example.vbook.domain.player.port.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//
//
////note for my self. It must be service scoped becose of player release on destroy
@Module
@InstallIn(ServiceComponent::class)
class MediaPlayerModule {
//
//    @Provides
//    @ServiceScoped
//    fun provideAudioAttributes() = AudioAttributes.Builder()
//        .setContentType(C.CONTENT_TYPE_SPEECH)
//        .setUsage(C.USAGE_MEDIA)
//        .build()
//
//    @Provides
//    @ServiceScoped
//    fun provideExoPlayer(
//        @ApplicationContext context: Context,
//        audioAttributes: AudioAttributes
//    ) = ExoPlayer.Builder(context)
//        .setAudioAttributes(audioAttributes, true)
//        .setHandleAudioBecomingNoisy(true)
//        .build()

//    @Provides
//    @ServiceScoped
//    fun provideMediaSession(
//        @ApplicationContext context: Context
//    ) = MediaSessionCompat(context, "VBookService")


}

@Module
@InstallIn(SingletonComponent::class)
class SUKA {
    @Provides
    @Singleton
    fun provideMediaPlayer(@ApplicationContext context: Context): MediaPlayer =
        PlayerImpl(context)

}
