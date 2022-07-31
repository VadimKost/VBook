/*
 * Created by Vadim on 13.07.22, 12:15
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 10.07.22, 00:20
 *
 */

package com.example.vbook.domain.repository

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book

interface BookRepository {
    suspend fun fetchNewBooks(page: Int): ResourceState<List<Book>>

    suspend fun searchBooks(value: String, page: Int): ResourceState<List<Book>>

    suspend fun getFavoriteBooks(): ResourceState<List<Book>>

    suspend fun setIsBookFavorite(bookUrl: String, isFavorite: Boolean)

    suspend fun savePlaybackPosition(book: Book)

    suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book>

}