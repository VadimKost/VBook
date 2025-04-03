/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.book.port

import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.shared.ResourceState
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getNewBooks(page: Int): Flow<ResourceState<List<Book>>>
    fun getBookByInAppId(inAppId: String): Flow<ResourceState<Book>>
    suspend fun updateBook(book: Book)
//    suspend fun searchBooks(query: String, page: Int): ResourceState<List<AudioBook>>
//    fun observeAudioBook(id: ExternalResourceId): Flow<ResourceState<AudioBook>>

}