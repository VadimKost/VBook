package com.example.vbook.presentation.newbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetPartOfNewBooksUseCase
import com.example.vbook.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewBooksVM @Inject constructor(
    val getPartOfNewBooks: GetPartOfNewBooksUseCase,
):ViewModel() {
    init {
        loadMoreNewBooks()
    }
    private val _booksState = MutableStateFlow<UiState<List<Book>>>(UiState.Loading)
    val booksState =_booksState.asStateFlow()

    private val _isRefreshing= MutableStateFlow(false)
    val isRefreshing= _isRefreshing.asStateFlow()

    private val _canBeRefreshed = MutableStateFlow(false)
    val canBeRefreshed = _canBeRefreshed.asStateFlow()

    private var bookList= listOf<Book>()
    var page=1



    fun loadMoreNewBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            val books= getPartOfNewBooks(page)
            when(books){
                is Result.Success -> {
                    page+=1
                    bookList= bookList.plus(books.data)
                    _canBeRefreshed.value = false
                    _booksState.value = UiState.Success(bookList)
                }
                is Result.Error -> {
                    _canBeRefreshed.value = true
                    _booksState.value= UiState.Error(books.message)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            if (
                _booksState.value is UiState.Error ||
                _booksState.value is UiState.Empty
            ){
                _isRefreshing.value =true
                delay(2000L)
                loadMoreNewBooks()
                _isRefreshing.value = false
            }

        }


    }
}
