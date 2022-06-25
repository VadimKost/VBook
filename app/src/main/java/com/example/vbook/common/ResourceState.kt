package com.example.vbook.common

sealed class ResourceState<out T> {
    class Success<T>(var data: T) : ResourceState<T>()
    object Loading : ResourceState<Nothing>()
    object Empty : ResourceState<Nothing>()
    class Error(val message: String) : ResourceState<Nothing>()

    fun onSuccess(action: (T) -> Unit): ResourceState<T> {
        if (this is Success) action(this.data)
        return this
    }

    fun onError(action: (String) -> Unit): ResourceState<T> {
        if (this is Error) action(this.message)
        return this
    }

    fun getOrNull(): T? {
        return if (this is Success) this.data else null
    }

}
