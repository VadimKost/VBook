/*
 * Created by Vadim on 16.07.22, 00:56
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 00:56
 *
 */

package com.example.vbook.domain.usecase.downloadingitem

import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.DownloadingItemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateDownloadingItemUseCase @Inject constructor(
    val downloadingItemRepository: DownloadingItemRepository,
) {
    suspend operator fun invoke(uri: String, downloadId:Long, book: Book) =
        downloadingItemRepository.createDownloadingItem(book.bookUrl,downloadId,uri)
}