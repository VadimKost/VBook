package com.example.vbook.data.repository

import com.example.vbook.data.model.Book
import com.example.vbook.data.parsers.BooksParser
import com.example.vbook.data.parsers.KnigaVUheParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    val knigaVUheParser: KnigaVUheParser
){
    val parsers = listOf<BooksParser>(knigaVUheParser)


    suspend fun addBooks(page:Int):MutableSet<Book> {
        return withContext(Dispatchers.IO) {
            parsers.first().getAllBookList(page).toMutableSet()
        }
    }

    suspend fun getBookDetailed(book: Book): Book {
        var bookDetailed=book
        withContext(Dispatchers.IO){
            bookDetailed=when(book.source){
                KnigaVUheParser.TAG-> knigaVUheParser.getBookDetailed(book)
                else -> book
            }
        }
        return bookDetailed
    }
}