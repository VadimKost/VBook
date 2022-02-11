package com.example.vbook.data.repository.BookRepository

import com.example.vbook.common.ResourceState
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.mapper.toBook
import com.example.vbook.data.mapper.toBookEntity
import com.example.vbook.data.mapper.toBookEntityList
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.data.repository.BookRepository.BookRepository
import com.example.vbook.presentation.model.Book
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
            val booksEntity = books.toBookEntityList()
            DB.bookDao().insert(booksEntity)
            if (books.isEmpty()) return ResourceState.Empty
            return ResourceState.Success(books)
        } catch (t: Throwable) {
            return ResourceState.Error(t.toString())
        }
    }

    override suspend fun getFilledBook(
        bookUrl: String,
    ): ResourceState<Book> {
        try {
            val book = DB.bookDao().getBook(bookUrl)?.toBook()
            if (book != null) {
                if (book.isDetailed()) {
                    return ResourceState.Success(book)
                } else {
                    val bookDetailed = knigaVUheParser.getFilledBook(book)
                    updateBook(book)
                    return ResourceState.Success(bookDetailed)
                }
            } else {
                return ResourceState.Empty
            }
        } catch (t: Throwable) {
            return ResourceState.Error(t.toString())
        }

    }

    override suspend fun getCurrentBook(): ResourceState<Book> {
        try {
            val book = DB.bookDao().getCurrentBook()?.toBook()
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
        val count = DB.bookDao().updateBook(book.toBookEntity())
        return count >= 1
    }


}

