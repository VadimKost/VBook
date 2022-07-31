/*
 * Created by Vadim on 31.07.22, 19:43
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 22.07.22, 12:31
 *
 */

package com.example.vbook.domain.usecase.mediaplayer.playback_controll

import com.example.vbook.domain.bookplayer.BookPlayer
import com.example.vbook.domain.bookplayer.TransferringData
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecase.mediaplayer.GetPlayerStateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReplaceMediaItemSourceUriUseCase @Inject constructor(
    val player: BookPlayer,
    val getPlayerStateUseCase: GetPlayerStateUseCase
) {
    suspend operator fun invoke(localBook: Book, index: Int, newUri: String): Boolean {
        var replaced = false
        val state = getPlayerStateUseCase().value
        if(state is TransferringData){
            if (state.data.book == localBook){
                replaced = player.replaceBookMediaItemUriSource(localBook, index, newUri)
            }
        }
        return replaced
    }

}