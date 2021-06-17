package com.example.vbook.data.repository

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
    val knigaVUheParser: KnigaVUheParser
): BookRepository {
    val parsers = listOf<BooksParser>(knigaVUheParser)

    override suspend fun getBooks(page:Int): Flow<Resurce<List<Book>>> {
        return flow {
            val books=parsers.first().getAllBookList(page)
            inMemoryStorage.books.apply {
                addAll(books)
                distinct()
            }
          emit(Resurce.Success(books))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getBookDetailed(book: Book): Flow<Resurce<Book>> {
        return flow {
            val bookDetailed:Book =when(book.source){
                    KnigaVUheParser.TAG-> knigaVUheParser.getBookDetailed(book)
                    else -> book
            }
            emit(Resurce.Success(bookDetailed))
        }.flowOn(Dispatchers.IO)
    }

}

