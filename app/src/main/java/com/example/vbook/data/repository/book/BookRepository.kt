package com.example.vbook.data.repository.book

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book

interface BookRepository {
    suspend fun fetchNewBooks(page:Int): ResourceState<List<Book>>

    suspend fun searchBooks(value:String,page: Int): ResourceState<List<Book>>

    suspend fun getFavoriteBooks(): ResourceState<List<Book>>

    suspend fun setIsBookFavorite(bookUrl: String,isFavorite:Boolean)

    suspend fun saveBookTimeLine(book: Book)

    suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book>
}