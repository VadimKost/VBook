package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResource
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetFilledBook @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend operator fun invoke(
        title: String,
        author: Pair<String, String>,
        reader: Pair<String, String>
    ): Resource<Book> {
        return try {
            bookRepository.getFilledBook(title, author, reader)
        }catch (e:Throwable){
            ThrowableResource(e,"GetBookDetailed")
        }
    }
}