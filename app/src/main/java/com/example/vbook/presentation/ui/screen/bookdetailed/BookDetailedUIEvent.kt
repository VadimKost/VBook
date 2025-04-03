/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.bookdetailed

import com.example.vbook.domain.player.port.MediaPlayer


sealed interface BookDetailedUIEvent {

    data class OnPlaybackControl(val command: MediaPlayer.PlaybackControlCommand) : BookDetailedUIEvent
    data object OnDownloadClick : BookDetailedUIEvent
    data class OnFavoriteClick(val isFavorite: Boolean) : BookDetailedUIEvent
    data class OnDownloadDialogToggle(val show: Boolean) : BookDetailedUIEvent
}
