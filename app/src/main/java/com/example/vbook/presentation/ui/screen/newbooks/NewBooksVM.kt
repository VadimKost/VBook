/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.newbooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.application.usecase.GetNewBooksUseCase
import com.example.vbook.domain.shared.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: rewrite stateModel
@HiltViewModel
open class NewBooksVM @Inject constructor(
    private val getNewBooksUseCase: GetNewBooksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewBooksUiState())
    val state: StateFlow<NewBooksUiState> = _state
        .onStart { loadMoreNewBooks() }
        .stateIn(viewModelScope, SharingStarted.Lazily, NewBooksUiState())

    private var page = 1

    private fun loadMoreNewBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            val books = getNewBooksUseCase(page).collect{ books ->
                when (books) {
                    is ResourceState.Success -> {
                        page += 1
                        _state.update {
                            it.copy(
                                books = it.books + books.data,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }

                    is ResourceState.Error -> {
                        _state.update {
                            it.copy(
                                errorMessage = books.message,
                                isLoading = false
                            )
                        }
                    }

                    is ResourceState.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }

        }
    }

    private fun refresh() {
        viewModelScope.launch {
            if (
                _state.value.errorMessage != null ||
                _state.value.books.isEmpty()
            ) {
                _state.update { it.copy(isRefreshing = true) }
                delay(2000L)
                loadMoreNewBooks()
                _state.update { it.copy(isRefreshing = false) }
            }

        }
    }

    fun onEvent(event: NewBooksUIEvent) {
        when (event) {
            is NewBooksUIEvent.OnRefresh -> refresh()
            is NewBooksUIEvent.OnAddMore -> loadMoreNewBooks()
            else -> {}
        }
    }
}
