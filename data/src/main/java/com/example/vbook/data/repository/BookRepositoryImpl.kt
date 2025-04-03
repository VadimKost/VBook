/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 10.07.22, 00:13
 *
 */

package com.example.vbook.data.repository

import android.util.Log
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.db.mapper.BookMapper.toDomain
import com.example.vbook.data.db.mapper.BookMapper.toEntity
import com.example.vbook.data.db.model.book.ExternalBookEntityId
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.data.parsers.Source
import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.book.port.BookRepository
import com.example.vbook.domain.shared.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    database: AppDatabase,
    private val knigaVUheParser: KnigaVUheParser
) : BookRepository {

    private val bookDAO = database.bookDao()

    override suspend fun getNewBooks(page: Int): Flow<ResourceState<List<Book>>> = flow {
        emit(ResourceState.Loading)
        try {
            val books = knigaVUheParser.getNewBooks(page)
            books.forEach {
                val internalId = it.key
                val book = it.value
                bookDAO.upsertDetailedBook(
                    book.toEntity().copy(
                        externalIds = listOf(
                            ExternalBookEntityId(
                                inAppBookId = book.inAppId,
                                source = Source.KnigaVUhe,
                                externalId = internalId
                            )
                        )
                    )
                )
            }
            emit(ResourceState.Success(books.values.toList()))
        } catch (t: Throwable) {
            Log.e("BookError", t.stackTraceToString())
            emit(ResourceState.Error("$t fetchNewBooks"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBookByInAppId(inAppId: String): Flow<ResourceState<Book>> = flow {
        val cachedBook = bookDAO.getBookByInAppId(inAppId)
        emit(ResourceState.Success(cachedBook.toDomain()))
        if (BookDetailingUpdatePolicy().shouldUpdate(cachedBook)) {
            val voiceovers = knigaVUheParser.getBookDetails(
                cachedBook.externalIds.first { it.source == Source.KnigaVUhe }.externalId
            )
            emit(
                ResourceState.Success(
                    cachedBook.copy(voiceovers = voiceovers.map { it.toEntity() }).toDomain()
                )
            )
//            bookDAO.upsertDetailedBook()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateBook(book: Book) {
        bookDAO.upsertDetailedBook(book.toEntity())
    }


//
//    override suspend fun searchBooks(query: String, page: Int): ResourceState<List<Book>> {
//        try {
//            val books = knigaVUheParser.search(query, page)
//            val booksEntity = books.mapToData()
//            DB.bookDao().insert(booksEntity)
//            return ResourceState.Success(books)
//        } catch (t: Throwable) {
//            Log.e("BookError", t.stackTraceToString())
//            return ResourceState.Error(t.toString() + " searchBooks")
//        }
//    }
//
//    override fun getFilledBook(
//        bookUrl: String,
//    ): Flow<ResourceState<Book>> = flow {
//        DB.bookDao().getBookByUrl(bookUrl).collect { bookEntity ->
//            val book = bookEntity?.mapToDomain()
//            if (book != null) {
//                if (book.isDetailed()) {
//                    emit(ResourceState.Success(book))
//                } else {
//                    val bookDetailed = knigaVUheParser.getFilledBook(book)
////                    savePlaybackPosition(book)
//                    emit(ResourceState.Success(bookDetailed))
//                }
//            } else {
//                emit(ResourceState.Error("Book was not found"))
//            }
//        }
//    }.catch {
//        Log.e("Book", it.stackTraceToString())
//        emit(ResourceState.Error("$it getFilledBook"))
//    }


//    override suspend fun getBookByUrl(url: String): ResourceState<Book> {
//        val bookEntity = DB.bookDao().getBookByUrl(url)
//        if (bookEntity != null){
//            return ResourceState.Success(bookEntity.mapToDomain())
//        }else{
//            return ResourceState.Error("There is no book with url $url")
//        }
//    }
//
//    override suspend fun getFavoriteBooks(): ResourceState<List<Book>> {
//        try {
//            val books = DB.bookDao().getFavoriteBooks().mapToDomain()
//            return ResourceState.Success(books)
//
//        } catch (t: Throwable) {
//            return ResourceState.Error(t.toString())
//        }
//    }
//
//    override suspend fun setIsBookFavorite(bookUrl: String, isFavorite: Boolean) {
//        DB.bookDao().setFavoriteBook(bookUrl, isFavorite)
//    }
//
//
//    override suspend fun savePlaybackPosition(book: Book) {
//        DB.bookDao()
//            .savePlaybackPosition(book.bookUrl, book.stoppedTrackIndex, book.stoppedTrackTime)
//    }


}

