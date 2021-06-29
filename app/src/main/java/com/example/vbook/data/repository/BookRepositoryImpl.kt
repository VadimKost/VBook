package com.example.vbook.data.repository

import android.util.Log
import com.example.vbook.data.parsers.BooksParser
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.domain.common.InMemoryStorage
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    val inMemoryStorage: InMemoryStorage,
    val parsers:List<BooksParser>
): BookRepository {


    override suspend fun getBooks(page:Int): Flow<Resurce<List<Book>>> {
        return flow {
            val books=parsers.first().getAllBookList(page)
            val difference=books-inMemoryStorage.books.toSet()
            Log.e("VVVd",difference.toString())
            inMemoryStorage.books.addAll(difference)
          emit(Resurce.Success(books))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookDetailed(book: Book): Flow<Resurce<Book>> {
        return flow {
            var bookDetailed:Book
            for(parser in parsers){
                if (parser.TAG == book.source){
                    bookDetailed=parser.getBookDetailed(book)
                    emit(Resurce.Success(bookDetailed))
                }else{
                    throw NullPointerException("Empty book")
                }
            }

        }.flowOn(Dispatchers.IO)
    }

}

