package com.example.vbook.data

import com.example.vbook.data.model.Book

class KnigaVUheParser(override var base_url: String) : BooksParser() {
    init {
        base_url="https://knigavuhe.org/"
    }
    override fun getBooks(page: Int): List<Book> {
        var url=base_url+"new/?page=$page"
        var list:List<Book> = listOf()




        return list
    }

    override fun search(text: String, page: Int): List<Book> {
        TODO("Not yet implemented")
    }
}