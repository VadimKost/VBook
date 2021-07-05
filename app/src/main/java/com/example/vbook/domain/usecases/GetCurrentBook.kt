package com.example.vbook.domain.usecases

import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentBook @Inject constructor(
    val bookRepository: BookRepository
){
    suspend operator fun invoke()=bookRepository.getCurrentBook()
}