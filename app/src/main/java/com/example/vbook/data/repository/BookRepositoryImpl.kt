package com.example.vbook.data.repository

import android.util.Log
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.data.parsers.BooksParser
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    val DB:AppDatabase,
    val knigaVUheParser: KnigaVUheParser
): BookRepository {

    override suspend fun fetchNewBooks(page:Int): Flow<Resurce<List<Book>>> {
        return flow {
            val books=knigaVUheParser.getAllBookList(page)
            Log.e("VVV",books.toString())
            Log.e("VVV",BookEntity.fromBookList(books).toString())
            DB.userDao().insert(BookEntity.fromBookList(books))

            emit(Resurce.Success(books))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookDetailed(book: Book): Flow<Resurce<Book>> {
        return flow {
            val bookDetailed=knigaVUheParser.getBookDetailed(book)
            DB.userDao().updateBook(BookEntity.fromBook(bookDetailed))

            DB.userDao().getBook(bookDetailed.title,bookDetailed.author).collect {
                emit(Resurce.Success(BookEntity.toBook(it)))
            }

        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getCurrentBook(): Flow<Resurce<Book>> {
        return flow {
            DB.userDao().getCurrentBook().collect{
                emit(Resurce.Success(BookEntity.toBook(it)))
            }
        }.flowOn(Dispatchers.IO)
    }

}

