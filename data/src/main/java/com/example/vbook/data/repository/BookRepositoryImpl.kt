/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 10.07.22, 00:13
 *
 */

package com.example.vbook.data.repository

import android.util.Log
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.data.db.mapper.mapToData
import com.example.vbook.data.db.mapper.mapToDomain
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    val DB: AppDatabase,
    val knigaVUheParser: KnigaVUheParser
) : BookRepository {

    override suspend fun fetchNewBooks(page: Int): ResourceState<List<Book>> {
        try {
            val books = knigaVUheParser.getAllNewBooks(page)
            val booksEntity = books.mapToData()
            DB.bookDao().insert(booksEntity)
            if (books.isEmpty()) return ResourceState.Empty
            return ResourceState.Success(books)
        } catch (t: Throwable) {
            Log.e("BookError", t.stackTraceToString())
            return ResourceState.Error(t.toString() + " fetchNewBooks")
        }
    }

    override suspend fun searchBooks(value: String, page: Int): ResourceState<List<Book>> {
        try {
            val books = knigaVUheParser.search(value, page)
            val booksEntity = books.mapToData()
            DB.bookDao().insert(booksEntity)
            if (books.isEmpty()) return ResourceState.Empty
            return ResourceState.Success(books)
        } catch (t: Throwable) {
            Log.e("BookError", t.stackTraceToString())
            return ResourceState.Error(t.toString() + " searchBooks")
        }
    }

    override suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book> {
        try {
            val book = DB.bookDao().getBookByUrl(bookUrl)?.mapToDomain()
            return if (book != null) {
                if (book.isDetailed()) {
                    ResourceState.Success(book)
                } else {
                    withContext(Dispatchers.IO){
                        val bookDetailed = knigaVUheParser.getFilledBook(book)
//                    savePlaybackPosition(book)
                        ResourceState.Success(bookDetailed)
                    }
                }
            } else {
                ResourceState.Empty
            }
        } catch (t: Throwable) {
            Log.e("Book", t.stackTraceToString())
            return ResourceState.Error(t.toString() + " getFilledBook")
        }

    }

//    override suspend fun getBookByUrl(url: String): ResourceState<Book> {
//        val bookEntity = DB.bookDao().getBookByUrl(url)
//        if (bookEntity != null){
//            return ResourceState.Success(bookEntity.mapToDomain())
//        }else{
//            return ResourceState.Error("There is no book with url $url")
//        }
//    }

    override suspend fun getFavoriteBooks(): ResourceState<List<Book>> {
        try {
            val books = DB.bookDao().getFavoriteBooks().mapToDomain()
            if (books.isNotEmpty()) {
                return ResourceState.Success(books)
            } else {
                return ResourceState.Empty
            }
        } catch (t: Throwable) {
            return ResourceState.Error(t.toString())
        }
    }

    override suspend fun setIsBookFavorite(bookUrl: String, isFavorite: Boolean) {
        DB.bookDao().setFavoriteBook(bookUrl, isFavorite)
    }


    override suspend fun savePlaybackPosition(book: Book) {
        DB.bookDao()
            .savePlaybackPosition(book.bookUrl, book.stoppedTrackIndex, book.stoppedTrackTime)
    }


}

