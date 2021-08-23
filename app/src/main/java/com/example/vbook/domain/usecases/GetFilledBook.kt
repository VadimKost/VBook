package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResult
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetFilledBook @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend operator fun invoke(
        bookUrl: String,
    ): Result<Book> {
        return try {
            bookRepository.getFilledBook(bookUrl)
        }catch (e:Throwable){
            ThrowableResult(e,"GetBookDetailed")
        }
    }
}