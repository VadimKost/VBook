package com.example.vbook.domain.usecases

import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBook @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: Book) =bookRepository.updateBook(book)
}