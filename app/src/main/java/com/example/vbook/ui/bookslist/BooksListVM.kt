package com.example.vbook.ui.bookslist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vbook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val booksRepository: BookRepository
):ViewModel() {
    var page=1
    val allBooks=booksRepository.allBooks

    init {
        runBlocking {
            loadMoreBooks()
        }
    }

    suspend fun loadMoreBooks(){
        Log.e("VVV", page.toString())
        booksRepository.addBooks(page)
        ++page
    }

}