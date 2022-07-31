package com.example.vbook.data.parsers

import com.example.vbook.domain.model.Book

abstract class BooksParser {
    abstract val base_url: String
    abstract val TAG: String

    abstract fun getAllNewBooks(page:Int):List<Book>

    abstract fun getFilledBook(book: Book): Book

    abstract fun search(text:String,page: Int):List<Book>

    abstract fun parseBookList(URL: String): MutableList<Book>

}