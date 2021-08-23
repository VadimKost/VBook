package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book

interface BookRepository {
    suspend fun fetchNewBooks(page:Int): Result<List<Book>>

    suspend fun getCurrentBook(): Result<Book>

    suspend fun updateBook(book: Book):Boolean

    suspend fun getFilledBook(
        bookUrl: String,
    ): Result<Book>
}