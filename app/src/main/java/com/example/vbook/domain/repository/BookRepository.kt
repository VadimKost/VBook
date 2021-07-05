package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book

interface BookRepository {
    suspend fun fetchNewBooks(page:Int): Resource<List<Book>>
    suspend fun getBookDetailed(book: Book): Resource<Book>
    suspend fun getCurrentBook(): Resource<Book>
    suspend fun getBook(title:String,author:Pair<String,String>):Resource<Book>

    suspend fun updateBook(book: Book):Boolean
}