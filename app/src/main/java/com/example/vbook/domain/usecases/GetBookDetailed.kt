package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResource
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetBookDetailed @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: Book): Resource<Book> {
        return try {
            bookRepository.getBookDetailed(book)
        }catch (e:Throwable){
            ThrowableResource(e,"GetBookDetailed")
        }
    }
}