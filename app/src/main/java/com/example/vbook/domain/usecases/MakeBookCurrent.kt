package com.example.vbook.domain.usecases

import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton
//Check error(boolean?)
@Singleton
class MakeBookCurrent @Inject constructor(
    val bookRepository: BookRepository,
    val getCurrentBook: GetCurrentBook,
    val updateBook: UpdateBook
){
    suspend operator fun invoke(book: Book){
        var oldBook =getCurrentBook()
        if (oldBook is Resource.Success){
            oldBook = oldBook.also { it.data.isCurrent =false}
            updateBook(oldBook.data)
            }
        book.isCurrent= true
        updateBook(book)
        }
    }
