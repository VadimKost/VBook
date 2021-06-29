package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableHandler
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetBookDetailed @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend fun execute(book: Book) =
        bookRepository.getBookDetailed(book)
            .catch { e ->
                emit(ThrowableHandler(e))
            }
}