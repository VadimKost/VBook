/*
 * Created by Vadim on 15.01.23, 22:33
 * Copyright (c) 2023 . All rights reserved.
 * Last modified 31.07.22, 20:47
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