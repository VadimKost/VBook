package com.example.vbook.data

import com.example.vbook.data.model.Book
import kotlinx.coroutines.flow.MutableStateFlow

object MemoryStorage {
    val allBooks = MutableStateFlow<Set<Book>>(mutableSetOf())
    val serchedBooks =MutableStateFlow<Set<Book>>(mutableSetOf())
}