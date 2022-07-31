/*
 * Created by Vadim on 16.07.22, 02:51
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 02:51
 *
 */

package com.example.vbook.domain.usecase.book

import com.example.vbook.domain.repository.BookRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchBookUseCase @Inject constructor(
    val bookRepository: BookRepository,
) {
    suspend operator fun invoke(value:String,page:Int) = bookRepository.searchBooks(value, page)
}