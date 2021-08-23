package com.example.vbook.presentation.bookslist

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val getPartOfNewBooks: GetPartOfNewBooks,
):ViewModel() {
    init {
        loadMoreNewBooks()
    }
    private val _booksState = mutableStateOf<UiState<List<Book>>>(UiState.Loading)
    val booksState:State<UiState<List<Book>>> =_booksState

    private var bookList= listOf<Book>()

    var page=1

    fun loadMoreNewBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            val books= getPartOfNewBooks(page)
            when(books){
                is Result.Success -> {
                    page+=1
                    bookList= bookList.plus(books.data)
                    _booksState.value= UiState.Data(bookList)
                }
                is Result.Error -> {}
            }
        }

    }
}
