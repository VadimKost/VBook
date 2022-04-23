package com.example.vbook.data.repository.mediaitem

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.common.model.DownloadingItem

interface DownloadingItemRepository {
    suspend fun createDownloadingItem(uri: String,book: Book)

    fun getDownloadStatus(downloadId: Long): ResourceState<Unit>
    suspend fun getBookDownloadingItemsStatus(book: Book): Map<String, ResourceState<Unit>>

    suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<DownloadingItem>>
    suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): ResourceState<DownloadingItem>
    suspend fun getMediaItemDownloadByOnlineUri(uri: String): ResourceState<DownloadingItem>

    suspend fun getAppropriateUri(uri: String): String
}