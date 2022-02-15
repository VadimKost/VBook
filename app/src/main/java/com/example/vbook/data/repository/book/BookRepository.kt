package com.example.vbook.data.repository.book

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book

interface BookRepository {
    suspend fun fetchNewBooks(page:Int): ResourceState<List<Book>>

    suspend fun getCurrentBook(): ResourceState<Book>

    suspend fun updateBook(book: Book):Boolean

    suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book>
}