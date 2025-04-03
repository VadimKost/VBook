/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.player.model

import com.example.vbook.domain.shared.model.MediaItem

data class Playlist(
    val name: String,
    val cover: String,
    val mediaItems: List<MediaItem>
)