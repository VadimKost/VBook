package com.example.vbook.data.parsers

import com.example.vbook.data.model.Book

abstract class BooksParser {
    abstract val base_url: String

    abstract fun getBooks(page:Int):List<Book>

    abstract fun search(text:String,page: Int):List<Book>


}