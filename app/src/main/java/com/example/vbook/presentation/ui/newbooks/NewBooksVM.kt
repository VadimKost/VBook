package com.example.vbook.presentation.ui.newbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.data.repository.BookRepository.BookRepository
import com.example.vbook.presentation.model.Book
import com.example.vbook.common.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBooksVM @Inject constructor(
    val bookRepository: BookRepository
) : ViewModel() {
    init {
        loadMoreNewBooks()
    }

    private val _booksState = MutableStateFlow<ResourceState<List<Book>>>(ResourceState.Loading)
    val booksState = _booksState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _canBeRefreshed = MutableStateFlow(false)
    val canBeRefreshed = _canBeRefreshed.asStateFlow()

    private var bookList = listOf<Book>()
    var page = 1


    fun loadMoreNewBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val books = bookRepository.fetchNewBooks(page)){
                is ResourceState.Success -> {
                    page += 1
                    bookList = bookList.plus(books.data)
                    _canBeRefreshed.value = false
                    _booksState.value = ResourceState.Success(bookList)
                }
                is ResourceState.Error -> {
                    _canBeRefreshed.value = true
                    _booksState.value = ResourceState.Error("Something went wrong")
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (
                _booksState.value is ResourceState.Error ||
                _booksState.value is ResourceState.Empty
            ) {
                _isRefreshing.value = true
                delay(2000L)
                loadMoreNewBooks()
                _isRefreshing.value = false
            }

        }


    }
}
