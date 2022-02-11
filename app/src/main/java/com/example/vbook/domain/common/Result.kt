package com.example.vbook.domain.common


sealed class Result<out T> {
    class Success<T>(var data:T): Result<T>()
    object Empty : Result<Nothing>()
    class Error(var message: String): Result<Nothing>()
}
