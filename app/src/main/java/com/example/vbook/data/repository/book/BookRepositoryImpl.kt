package com.example.vbook.data.repository.book

import com.example.vbook.common.ResourceState
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.common.model.Book
import com.example.vbook.data.db.mapper.mapToData
import com.example.vbook.data.db.mapper.mapToDomain
import com.example.vbook.isDetailed
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
            return ResourceState.Error(t.toString()+" fetchNewBooks")
        }
    }

    override suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book> {
        try {
            val book = DB.bookDao().getBook(bookUrl)?.mapToDomain()
            return if (book != null) {
                if (book.isDetailed()) {
                    ResourceState.Success(book)
                } else {
                    val bookDetailed = knigaVUheParser.getFilledBook(book)
                    updateBook(book)
                    ResourceState.Success(bookDetailed)
                }
            } else {
                ResourceState.Empty
            }
        } catch (t: Throwable) {
            return ResourceState.Error(t.toString() +" getFilledBook")
        }

    }

    override suspend fun getCurrentBook(): ResourceState<Book> {
        try {
            val book = DB.bookDao().getCurrentBook()?.mapToDomain()
            if (book != null) {
                return ResourceState.Success(book)
            } else {
                return ResourceState.Empty
            }
        } catch (t: Throwable) {
            return ResourceState.Error(t.toString())
        }
    }


    override suspend fun updateBook(book: Book): Boolean {
        val count = DB.bookDao().updateBook(book.mapToData())
        return count >= 1
    }


}

