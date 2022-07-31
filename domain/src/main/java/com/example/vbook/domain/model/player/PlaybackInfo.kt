package com.example.vbook.domain.model.player

data class PlaybackInfo(
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val trackIndex: Int = 0,
    val trackTime: Pair<Long, Long> = 0L to 0L
)