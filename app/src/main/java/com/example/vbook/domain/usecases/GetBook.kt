package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableHandler
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBook @Inject constructor(
    val booksRepository: BookRepository
) {
    suspend operator fun invoke(title:String,author:Pair<String,String>): Resource<Book> {
        return try {
            booksRepository.getBook(title, author)
        }catch (e:Throwable){
            ThrowableHandler(e,"GetBook")
        }
    }
}