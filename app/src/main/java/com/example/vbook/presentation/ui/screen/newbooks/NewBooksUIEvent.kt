/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.newbooks

import com.example.vbook.domain.book.model.Book

sealed interface NewBooksUIEvent {
    data object OnRefresh : NewBooksUIEvent
    data object OnAddMore : NewBooksUIEvent
    data class OnBookClick(val book: Book) : NewBooksUIEvent
}