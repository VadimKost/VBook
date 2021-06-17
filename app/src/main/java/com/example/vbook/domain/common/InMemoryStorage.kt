package com.example.vbook.domain.common

import com.example.vbook.domain.model.Book
import javax.inject.Inject
import javax.inject.Singleton

abstract class InMemoryStorage {
    abstract var books:MutableList<Book>
}