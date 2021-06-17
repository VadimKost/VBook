package com.example.vbook.data

import com.example.vbook.domain.common.InMemoryStorage
import com.example.vbook.domain.model.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryStorageImpl @Inject constructor(): InMemoryStorage() {
    override var books: MutableList<Book> = mutableListOf()
}