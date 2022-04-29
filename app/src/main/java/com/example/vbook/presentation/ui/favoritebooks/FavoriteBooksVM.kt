package com.example.vbook.presentation.ui.favoritebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteBooksVM @Inject constructor(
    val booksRepository: BookRepository
): ViewModel() {
    init {
        getFavoriteBooks()
    }

    private val _booksState = MutableStateFlow<ResourceState<List<Book>>>(ResourceState.Loading)
    val booksState = _booksState.asStateFlow()

    fun getFavoriteBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            _booksState.value = booksRepository.getFavoriteBooks()
        }
    }
}