package com.example.vbook.presentation.ui.favoritebooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecase.book.GetFavoriteBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteBooksVM @Inject constructor(
    val getFavoriteBooksUseCase: GetFavoriteBooksUseCase
): ViewModel() {
    init {
        getFavoriteBooks()
    }

    private val _booksState = MutableStateFlow<ResourceState<List<Book>>>(
        ResourceState.Loading)
    val booksState = _booksState.asStateFlow()

    fun getFavoriteBooks(){
        viewModelScope.launch(Dispatchers.IO) {
            _booksState.value = getFavoriteBooksUseCase()
        }
    }
}