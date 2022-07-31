/*
 * Created by Vadim on 31.07.22, 20:34
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 31.07.22, 20:34
 *
 */

package com.example.vbook.domain.usecase.book

import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePlaybackPositionUseCase @Inject constructor(
    val bookRepository: BookRepository
) {
    suspend operator fun invoke(book: Book) {
        bookRepository.savePlaybackPosition(book)
    }
}