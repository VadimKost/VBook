/*
 * Created by Vadim on 15.01.23, 22:33
 * Copyright (c) 2023 . All rights reserved.
 * Last modified 31.07.22, 20:47
 *
 */

package com.example.vbook.domain.repository

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.DownloadingItem

interface DownloadingItemRepository {
    suspend fun createDownloadingItem(bookUrl: String, downloadId: Long, mediaUri:String)

    suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<DownloadingItem>>
    suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): ResourceState<DownloadingItem>
    suspend fun getMediaItemDownloadByOnlineUri(uri: String): ResourceState<DownloadingItem>

}