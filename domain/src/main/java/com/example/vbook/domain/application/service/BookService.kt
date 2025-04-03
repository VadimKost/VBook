/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.application.service

import com.example.vbook.domain.book.port.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookService @Inject constructor(
    private val bookRepository: BookRepository,
) {
    suspend fun getNewBooks(page: Int) = bookRepository.getNewBooks(page)

    fun observeBookByInAppId(inAppId: String) = bookRepository.getBookByInAppId(inAppId)
}