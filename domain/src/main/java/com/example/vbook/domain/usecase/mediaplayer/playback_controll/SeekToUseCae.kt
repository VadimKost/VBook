/*
 * Created by Vadim on 31.07.22, 19:55
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 31.07.22, 19:55
 *
 */

package com.example.vbook.domain.usecase.mediaplayer.playback_controll

import com.example.vbook.domain.bookplayer.BookPlayer
import com.example.vbook.domain.bookplayer.TransferringData
import com.example.vbook.domain.usecase.mediaplayer.GetPlayerStateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeekToUseCae @Inject constructor(
    val getPlayerStateUseCase: GetPlayerStateUseCase,
    val player: BookPlayer
) {
    operator fun invoke(index: Int, time: Long) {
        val state = getPlayerStateUseCase().value
        if (state is TransferringData) player.seekTo(index, time)
    }
}