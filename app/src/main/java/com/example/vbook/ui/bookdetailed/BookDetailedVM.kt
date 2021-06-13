package com.example.vbook.ui.bookdetailed

import androidx.lifecycle.ViewModel
import com.example.vbook.data.model.Book
import com.example.vbook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    val booksRepository: BookRepository
): ViewModel() {
    val bookList= booksRepository.newBookData

    suspend fun getBookDetailed(book:Book):Book{
        return booksRepository.getBookDetailed(book)
    }
}