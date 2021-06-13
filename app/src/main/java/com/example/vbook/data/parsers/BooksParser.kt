package com.example.vbook.data.parsers

import com.example.vbook.data.model.Book

abstract class BooksParser {
    abstract val base_url: String

    abstract fun getAllBookList(page:Int):List<Book>

    abstract fun getBookDetailed(book: Book):Book

    abstract fun search(text:String,page: Int):List<Book>

    abstract fun parseBookList(URL: String): MutableList<Book>
}