/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

//package com.example.vbook.presentation.ui.screen.favoritebooks
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.vbook.domain.ResourceState
//import com.example.vbook.domain.usecase.audiobook.GetFavoriteBooksUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class FavoriteBooksVM @Inject constructor(
//    private val getFavoriteBooksUseCase: GetFavoriteBooksUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(FavoriteBooksUIState())
//    val state: StateFlow<FavoriteBooksUIState> = _state
//        .onStart { getFavoriteBooks() }
//        .stateIn(viewModelScope, SharingStarted.Lazily, FavoriteBooksUIState())
//
//    fun getFavoriteBooks() {
//        viewModelScope.launch(Dispatchers.IO) {
//            when (val favoriteBooks = getFavoriteBooksUseCase()) {
//
//                is ResourceState.Success -> {
//                    _state.update {
//                        it.copy(books = favoriteBooks.data, isLoading = false)
//                    }
//                }
//
//                is ResourceState.Error -> {
//                    _state.update {
//                        it.copy(errorMessage = favoriteBooks.message, isLoading = false)
//                    }
//                }
//
//                else -> {}
//            }
//        }
//    }
//
//
//}