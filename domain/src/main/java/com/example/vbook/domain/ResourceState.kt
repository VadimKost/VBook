package com.example.vbook.domain

sealed class ResourceState<out T> {
    class Success<T>(var data: T) : ResourceState<T>() {
        override fun toString(): String {
            return "Success(data=$data)"
        }
    }

    object Loading : ResourceState<Nothing>()
    object Empty : ResourceState<Nothing>()
    class Error(val message: String) : ResourceState<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): ResourceState<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (String) -> Unit): ResourceState<T> {
        if (this is Error) action(message)
        return this
    }

    fun getOrNull(): T? {
        return if (this is Success) data else null
    }
}