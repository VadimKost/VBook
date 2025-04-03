/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.player.port

import com.example.vbook.domain.player.model.PlaybackState
import com.example.vbook.domain.player.model.Playlist
import kotlinx.coroutines.flow.StateFlow

interface MediaPlayer {
    fun observePlayerState(): StateFlow<State>
    fun handlePlaybackControlCommand(command: PlaybackControlCommand): Boolean
    suspend fun setPlaylist(playlist: Playlist): Boolean

    sealed class State {
        data object Initialization : State()
        data object Idle : State()
        data class Ready(val playbackState: PlaybackState) : State()
    }

    sealed class PlaybackControlCommand {
        data object Play : PlaybackControlCommand()
        data object Pause : PlaybackControlCommand()

        data object Next : PlaybackControlCommand()
        data object Previous : PlaybackControlCommand()

        data object Rewind : PlaybackControlCommand()
        data object FastForward : PlaybackControlCommand()

        data class SeekTo(val index: Int, val time: Long = 0) : PlaybackControlCommand()
    }


}