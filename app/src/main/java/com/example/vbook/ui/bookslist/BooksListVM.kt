package com.example.vbook.ui.bookslist

import androidx.lifecycle.ViewModel
import com.example.vbook.data.model.Book
import com.example.vbook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val booksRepository: BookRepository
):ViewModel() {
    var page=1
    val allNewBooks = MutableStateFlow(setOf<Book>())
    init {
        runBlocking {
            loadMoreNewBooks()
        }
    }

    suspend fun loadMoreNewBooks(){
        allNewBooks.value =allNewBooks.value.plus(booksRepository.addBooks(page))
        ++page
    }

}