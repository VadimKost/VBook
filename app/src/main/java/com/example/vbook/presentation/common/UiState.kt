package com.example.vbook.presentation.common

sealed class UiState<out T> {
    object Loading:UiState<Nothing>()
    class Error(val message:String):UiState<Nothing>()
    object Empty:UiState<Nothing>()
    class Success<T>(var data:T): UiState<T>()
}
