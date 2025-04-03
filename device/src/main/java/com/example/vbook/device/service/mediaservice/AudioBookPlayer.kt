/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.device.service.mediaservice

import android.content.ComponentName
import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.vbook.domain.player.model.PlaybackState
import com.example.vbook.domain.player.model.Playlist
import com.example.vbook.domain.player.port.MediaPlayer
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TICKER_DELAY: Long = 1000


class PlayerImpl @Inject constructor(
    @ApplicationContext val context: Context
) : MediaPlayer {
    //TODO: replace by right one
    val scope = CoroutineScope(Dispatchers.IO)

    private val state: MutableStateFlow<MediaPlayer.State> =
        MutableStateFlow(MediaPlayer.State.Initialization)

    private val sessionToken =
        SessionToken(context, ComponentName(context, PlaybackService::class.java))

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var ticker: Flow<Unit> = flow { emit(Unit); delay(TICKER_DELAY) }
    private var player: Player? = null
    private var currentPlaylist: MutableStateFlow<Playlist?> = MutableStateFlow(null)
    private var updateJob: Job? = null

    init {
        scope.launch {
            state.subscriptionCount.collect {
                if (it >= 1 && state.value == MediaPlayer.State.Initialization) {
                    player = getMediaController()
                    withContext(Dispatchers.Main) {
                        player?.prepare()
                    }
                    state.update { MediaPlayer.State.Idle }
                    startUpdatingPlayerState()
                } else if (it == 0) {
                    withContext(Dispatchers.Main) {
                        controllerFuture?.let { it1 -> MediaController.releaseFuture(it1) }
                        player = null
                    }
                    state.update { MediaPlayer.State.Initialization }
                    stopUpdatingPlayerState()
                }
            }
        }
    }

    override fun observePlayerState(): StateFlow<MediaPlayer.State> = state.asStateFlow()

    override fun handlePlaybackControlCommand(command: MediaPlayer.PlaybackControlCommand): Boolean {
        val player = player
        requireNotNull(player)
        when (command) {
            MediaPlayer.PlaybackControlCommand.Play -> player.play()
            MediaPlayer.PlaybackControlCommand.Pause -> player.pause()
            MediaPlayer.PlaybackControlCommand.Next -> player.seekToNext()
            else -> {}
        }
        return true
    }

    override suspend fun setPlaylist(playlist: Playlist): Boolean {
        val oldPlaylist = currentPlaylist.value
        return if (oldPlaylist != playlist) {
            currentPlaylist.update { playlist }
            player?.let {
                it.pause()
                it.clearMediaItems()
                it.addMediaItems(getMediaItems(playlist))
            }
            true
        } else false
    }

    private suspend fun getMediaController() = suspendCoroutine { continuation ->
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture?.let {
            it.addListener({
                continuation.resume(it.get())
            }, MoreExecutors.directExecutor())
        }
    }

    private fun getMediaItems(playlist: Playlist): List<MediaItem> {
        return playlist.mediaItems.map { mediaItem ->
            MediaItem.Builder()
                .setUri(mediaItem.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtworkUri(playlist.cover.toUri())
                        .setTitle(mediaItem.title)
                        .setArtist(playlist.name)
                        .build()
                )
                .build()
        }
    }

    private fun startUpdatingPlayerState() {
        val player = player
        requireNotNull(player)

        updateJob = scope.launch {
            combine(currentPlaylist, ticker) { playList, _ ->
                if (playList == null) return@combine state.value
                val playbackState = PlaybackState(
                    isPlaying = player.isPlaying,
                    playlist = playList,
                    currentTrackIndex = player.currentMediaItemIndex,
                    currentPosition = player.currentPosition
                )
                MediaPlayer.State.Ready(playbackState)
            }.collect {
                state.update { it }
            }
        }
    }

    private fun stopUpdatingPlayerState() {
        updateJob?.cancel()
        updateJob = null
    }
}

//player?.let {
//                        withContext(Dispatchers.Main) {
//                            it.addMediaItems(
//                                listOf(
//                                    MediaItem.Builder()
//                                        .setUri("https://cdn.pixabay.com/audio/2024/03/18/audio_b71ef0cb1f.mp3")
//                                        .setMediaMetadata(
//                                            MediaMetadata.Builder()
//                                                .setArtworkUri("https://play-lh.googleusercontent.com/bSiGSQYW9VMuqb3FLA3t3kQmG39GOJ3z_QtTxvbvFJVTPmvrlxrID1e32TSmUIsJNCM".toUri())
//                                                .build()
//                                        ).build(),
//                                    MediaItem.Builder()
//                                        .setUri("https://cdn.pixabay.com/audio/2025/01/21/audio_8903d9fb49.mp3")
//                                        .setMediaMetadata(
//                                            MediaMetadata.Builder()
//                                                .setArtworkUri("https://fishingclub.in.ua/upload/iblock/e58/e58a893a185ea553715b00f0e823ac24.jpg".toUri())
//                                                .build()
//                                        ).build(),
//                                )
//                            )
//                            it.prepare()
//                            it.play()
//                            Log.e("asd",it.mediaItemCount.toString())
//                        }
//                    }