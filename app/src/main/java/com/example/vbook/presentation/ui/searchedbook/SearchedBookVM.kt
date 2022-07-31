package com.example.vbook.presentation.ui.searchedbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecase.book.SearchBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchedBookVM @Inject constructor(
    val searchBookUseCase: SearchBookUseCase
) : ViewModel() {
    fun init(query:String) {
        loadMoreNewBooks(query)
    }

    private val _booksState = MutableStateFlow<ResourceState<List<Book>>>(
        ResourceState.Loading)
    val booksState = _booksState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _canBeRefreshed = MutableStateFlow(false)
    val canBeRefreshed = _canBeRefreshed.asStateFlow()

    private var bookList = listOf<Book>()
    private var page = 1

    fun loadMoreNewBooks(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val books = searchBookUseCase(value,page)
            when (books) {
                is ResourceState.Success -> {
                    page += 1
                    bookList = bookList.plus(books.data)
                    _canBeRefreshed.value = false
                    _booksState.value = ResourceState.Success(bookList)
                }
                is ResourceState.Error -> {
                    _canBeRefreshed.value = true
                    _booksState.value = ResourceState.Error(books.message)
                }
                else -> {}
            }
        }
    }

    fun refresh(searchedText:String) {
        viewModelScope.launch {
            if (
                _booksState.value is ResourceState.Error ||
                _booksState.value is ResourceState.Empty
            ) {
                _isRefreshing.value = true
                delay(2000L)
                loadMoreNewBooks(searchedText)
                _isRefreshing.value = false
            }

        }
    }
}