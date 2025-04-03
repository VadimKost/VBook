/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.searchedbook

import com.example.vbook.domain.book.model.Book

sealed interface SearchedBooksUIEvent {
    object OnRefresh : SearchedBooksUIEvent
    object OnAddMore : SearchedBooksUIEvent
    data class OnBookClick(val book: Book) : SearchedBooksUIEvent
}