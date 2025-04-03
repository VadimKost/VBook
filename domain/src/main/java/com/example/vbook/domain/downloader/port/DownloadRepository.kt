/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.downloader.port

import com.example.vbook.domain.shared.ResourceState
import com.example.vbook.domain.downloader.model.Download

interface DownloadRepository {
    suspend fun addDownloadingItem(bookUrl: String, downloadId: Long, mediaUri:String)
    suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<Download>>
    suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): ResourceState<Download>
    suspend fun getMediaItemDownloadByOnlineUri(uri: String): ResourceState<Download>

}