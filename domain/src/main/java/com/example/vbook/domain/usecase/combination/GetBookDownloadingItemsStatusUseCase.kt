/*
 * Created by Vadim on 16.07.22, 02:07
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 02:07
 *
 */

package com.example.vbook.domain.usecase.combination

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.DownloadingItem
import com.example.vbook.domain.usecase.booktrakdownloader.GetDownloadStatusByDownloadIdUseCase
import com.example.vbook.domain.usecase.booktrakdownloader.GetLocalUriByDownloadIdUseCase
import com.example.vbook.domain.usecase.downloadingitem.GetMediaItemDownloadsByBookUrlUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBookDownloadingItemsStatusUseCase @Inject constructor(
    val getMediaItemDownloadsByBookUrlUseCase: GetMediaItemDownloadsByBookUrlUseCase,
    val getDownloadStatusByDownloadIdUseCase: GetDownloadStatusByDownloadIdUseCase,
) {
    suspend operator fun invoke(book: Book): Map<String, DownloadingItem.Status> {
        val mediaUrls = book.mediaItems!!.map { it.second }
        val status = mutableMapOf<String, DownloadingItem.Status>()
        when (val downloadingItems = getMediaItemDownloadsByBookUrlUseCase(book.bookUrl)) {
            is ResourceState.Empty -> {
                mediaUrls.forEach { mediaUrl ->
                    status[mediaUrl] = DownloadingItem.Status.EMPTY
                }
                return status
            }
            is ResourceState.Success -> {
                mediaUrls.forEach { mediaUrl ->
                    if (downloadingItems.data.any { mediaUrl == it.onlineUri }) {
                        val downloadingItem =
                            downloadingItems.data.first { it.onlineUri == mediaUrl }

                        val downloadingState =
                            getDownloadStatusByDownloadIdUseCase(downloadingItem.downloadId)
                        status[mediaUrl] = downloadingState

                    } else {
                        status[mediaUrl] = DownloadingItem.Status.EMPTY
                    }
                }
                return status
            }
            else -> {}
        }
        return status
    }
}