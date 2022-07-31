/*
 * Created by Vadim on 17.07.22, 02:36
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 17.07.22, 02:36
 *
 */

package com.example.vbook.domain.usecase.mediaplayer

import com.example.vbook.domain.bookplayer.BookPlayer
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.DownloadingItemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPlayerStateUseCase @Inject constructor(
    val player: BookPlayer
) {
    operator fun invoke() = player.getPlayerState()
}