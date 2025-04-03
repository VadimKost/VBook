/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.book.model

// TODO: Move to interface to further mapping simplify or decide use plain mappers
data class Book(
    val inAppId: String,
    val title: String,
    val cover: String,
    val authors: List<Author>,
    val voiceovers: List<Voiceover>,
    val cycle: Cycle? = null,
)

