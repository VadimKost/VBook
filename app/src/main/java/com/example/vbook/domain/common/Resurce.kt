package com.example.vbook.domain.common


sealed class Resurce<out T>(){
    class Success<T>(var data:T): Resurce<T>()
    class Error<T>(var message:String): Resurce<T>()
}
