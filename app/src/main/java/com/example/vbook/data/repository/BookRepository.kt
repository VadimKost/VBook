package com.example.vbook.data.repository

import com.example.vbook.data.model.Book
import com.example.vbook.data.parsers.BooksParser
import com.example.vbook.data.parsers.KnigaVUheParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class BookRepository @Inject constructor(
    knigaVUheParser: KnigaVUheParser
){
    val parsers = listOf<BooksParser>(knigaVUheParser)

    val allBooks = MutableStateFlow<Set<Book>>(setOf())
    val searchedBooks = MutableStateFlow<Set<Book>>(setOf())

//  adds books from site page to flow
    suspend fun addBooks(page:Int) {
        withContext(Dispatchers.IO){
            allBooks.value= allBooks.value.plus(parsers.first().getBooks(page))
        }
    }
}