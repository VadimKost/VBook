package com.example.vbook.common

sealed class ResourceState<out T> {
    class Success<T>(var data: T) : ResourceState<T>()
    object Loading : ResourceState<Nothing>()
    object Empty : ResourceState<Nothing>()
    class Error(val message: String) : ResourceState<Nothing>()

}
