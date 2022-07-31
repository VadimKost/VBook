/*
 * Created by Vadim on 31.07.22, 19:42
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 22.07.22, 14:09
 *
 */

package com.example.vbook.domain.usecase.mediaplayer.playback_controll

import com.example.vbook.domain.bookplayer.BookPlayer
import com.example.vbook.domain.bookplayer.TransferringData
import com.example.vbook.domain.bookplayer.WaitingForData
import com.example.vbook.domain.usecase.mediaplayer.GetPlayerStateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayUseCase @Inject constructor(
    val getPlayerStateUseCase: GetPlayerStateUseCase,
    val player: BookPlayer
) {
    operator fun invoke() {
        val state = getPlayerStateUseCase().value
        if (state is TransferringData ) player.play()
    }
}