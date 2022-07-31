/*
 * Created by Vadim on 31.07.22, 19:43
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 22.07.22, 14:09
 *
 */

package com.example.vbook.domain.usecase.mediaplayer.playback_controll

import com.example.vbook.domain.bookplayer.BookPlayer
import com.example.vbook.domain.bookplayer.TransferringData
import com.example.vbook.domain.bookplayer.WaitingForData
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecase.mediaplayer.GetPlayerStateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetPlayerBookPlaylistUseCase @Inject constructor(
    val getPlayerStateUseCase: GetPlayerStateUseCase,
    val player: BookPlayer
) {
    suspend operator fun invoke(book: Book) {
        val state = getPlayerStateUseCase().value
        if (state is TransferringData || state is WaitingForData) player.setPlayerBookPlaylist(book)
    }
}