package com.example.vbook.data.repository

import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.mapper.toBook
import com.example.vbook.data.mapper.toBookEntity
import com.example.vbook.data.mapper.toBookEntityList
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.isDetailed
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    val DB:AppDatabase,
    val knigaVUheParser: KnigaVUheParser
): BookRepository {

    override suspend fun fetchNewBooks(page:Int): Resource<List<Book>> {
        val books=knigaVUheParser.getAllNewBooks(page)
        val booksEntity = books.toBookEntityList()
        DB.bookDao().insert(booksEntity)
        return Resource.Success(books)
    }

    override suspend fun getBook(
        title: String,
        author: Pair<String, String>,
        reader: Pair<String, String>
    ):Resource<Book>{
        val bookEntity= DB.bookDao().getBook(title, author, reader)
        if (bookEntity !=null){
            return Resource.Success(bookEntity.toBook())
        }else{
            return Resource.Error("Empty Book")
        }
    }

    override suspend fun getFilledBook(
        title: String,
        author: Pair<String, String>,
        reader: Pair<String, String>
    ): Resource<Book>{
        val bookEntity=DB.bookDao().getBook(title,author,reader)
        if (bookEntity != null) {
            if (bookEntity.isDetailed()){
                return Resource.Success(bookEntity.toBook())
            }else{
                val bookDetailed=knigaVUheParser.getFilledBook(bookEntity.toBook())
                DB.bookDao().insert(listOf(bookDetailed.toBookEntity()))
                return Resource.Success(bookDetailed)
            }
        }else{
            return Resource.Error("Empty Book")
        }
    }

    override suspend fun getCurrentBook(): Resource<Book> {
        val bookEntity = DB.bookDao().getCurrentBook()
        if (bookEntity != null){
            return Resource.Success(bookEntity.toBook())
        }else{
            return Resource.Error("NoCurrentBook")
        }
    }


    override suspend fun updateBook(book: Book):Boolean{
        val count = DB.bookDao().updateBook(book.toBookEntity())
        return count>=1
    }



}

