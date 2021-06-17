package com.example.vbook.domain.usecases

import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.repository.BookRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPartOfNewBooks @Inject constructor(
   val bookRepository: BookRepository
) {
    suspend fun execute(page:Int) =
        bookRepository.getBooks(page)
        .catch { e ->
            emit(Resurce.Error("Offline or other shit"))
        }
}