/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.bookdetailed

import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.player.port.MediaPlayer
import com.example.vbook.domain.shared.ResourceState


data class BookDetailedUIState(
    val book: ResourceState<Book> = ResourceState.Loading,
    val playerState: MediaPlayer.State = MediaPlayer.State.Initialization
//    val downloads: ResourceState<Map<String, DownloadingItem.Status>> = ResourceState.Loading,
//    val isCurrentlyPlayingBookSame: Boolean = false,
//    val isShowDownloadingDialog: Boolean = false,
)
