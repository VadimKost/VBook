package com.example.vbook.presentation.common

sealed class UiState<out T> {
    object Loading:UiState<Nothing>()
    object Error:UiState<Nothing>()
    object Empty:UiState<Nothing>()
    class Data<T>(var data:T): UiState<T>()
}
