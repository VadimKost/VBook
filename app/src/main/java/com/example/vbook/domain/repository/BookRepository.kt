package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(page:Int): Flow<Resurce<List<Book>>>
    suspend fun getBookDetailed(book: Book): Flow<Resurce<Book>>
}