package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResource
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPartOfNewBooks @Inject constructor(
   val bookRepository: BookRepository
) {
    suspend fun invoke(page:Int): Resource<List<Book>> {
        return try {
            bookRepository.fetchNewBooks(page)
        }catch (e:Throwable){
            ThrowableResource(e)
        }
    }
}