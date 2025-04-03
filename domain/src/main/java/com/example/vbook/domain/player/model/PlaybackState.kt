/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.player.model

data class PlaybackState(
    val isPlaying: Boolean,
    val playlist: Playlist,
    val currentTrackIndex: Int,
    val currentPosition: Long,
)