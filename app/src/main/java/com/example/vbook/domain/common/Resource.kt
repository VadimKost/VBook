package com.example.vbook.domain.common


sealed class Resource<T>(){
    class Success<T>(var data:T): Resource<T>()
    class Error<T>(var message: String): Resource<T>()
}
