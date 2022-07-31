/*
 * Created by Vadim on 16.07.22, 18:13
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 18:13
 *
 */

package com.example.vbook.domain.model.player

import com.example.vbook.domain.model.Book
import kotlinx.coroutines.flow.Flow

data class PlayerMetadata(
    val playbackInfo: PlaybackInfo,
    val book: Book
)
