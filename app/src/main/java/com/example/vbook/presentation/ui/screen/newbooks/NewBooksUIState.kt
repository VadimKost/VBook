/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.newbooks

import com.example.vbook.domain.book.model.Book

data class NewBooksUiState(
    val books: List<Book> = listOf(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)