package com.example.vbook.domain.usecases

import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBook @Inject constructor(
    val booksRepository: BookRepository
) {
    suspend operator fun invoke(title:String,author:Pair<String,String>)
        =booksRepository.getBook(title, author)
}