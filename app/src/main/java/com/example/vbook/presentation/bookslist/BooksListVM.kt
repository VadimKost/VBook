package com.example.vbook.presentation.bookslist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetPartOfNewBooks
import com.example.vbook.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val getPartOfNewBooks: GetPartOfNewBooks,
):ViewModel() {
    init {
        loadMoreNewBooks()
    }
    private val _booksState = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    val booksState =_booksState.asStateFlow()

    private var bookList= listOf<Book>()
    var page=1
    val isRefreshing= mutableStateOf(false)

    fun loadMoreNewBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            val books= getPartOfNewBooks(page)
            when(books){
                is Result.Success -> {
                    page+=1
                    bookList= bookList.plus(books.data)
                    _booksState.value= UiState.Data(bookList)
                }
                is Result.Error -> {
                    _booksState.value= UiState.Error(books.message)
                }
            }
        }

    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value =true
            delay(2000L)
            if (
                _booksState.value is UiState.Error ||
                _booksState.value is UiState.Empty
            ){
                loadMoreNewBooks()
            }
            isRefreshing.value = false
        }


    }
}
