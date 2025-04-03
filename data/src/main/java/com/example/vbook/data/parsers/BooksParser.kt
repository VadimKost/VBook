package com.example.vbook.data.parsers

import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.book.model.Voiceover


abstract class BooksParser {
    abstract val source: Source

    abstract suspend fun getNewBooks(page: Int): Map<String, Book>

    abstract suspend fun getBookDetails(internalId: String): List<Voiceover>

    abstract suspend fun getBooksInCycle(internalId: String): Map<String, Book>
//
//    abstract suspend fun getBookByAuthorAndTitle(author: String, title: String): Book?
//
//    abstract suspend fun search(text: String, page: Int): List<Book>
}

enum class Source(val baseUrl: String) {
    KnigaVUhe("https://knigavuhe.org")
}