/*
 * Created by Vadim on 16.07.22, 02:48
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 02:48
 *
 */

package com.example.vbook.domain.usecase.book

import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNewBooksUseCase @Inject constructor(
    val bookRepository: BookRepository,
) {
    suspend operator fun invoke(page:Int) =
        bookRepository.fetchNewBooks(page)
}