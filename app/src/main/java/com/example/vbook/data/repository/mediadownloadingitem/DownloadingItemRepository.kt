package com.example.vbook.data.repository.mediadownloadingitem

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.DownloadingItem

interface DownloadingItemRepository {
    suspend fun getMediaItemDownloadsByBookUrl(url:String):ResourceState<List<DownloadingItem>>
    suspend fun getMediaItemDownloadByDownloadId(downloadId:Long):ResourceState<DownloadingItem>
    suspend fun createMediaItemDownload(mediaOnlineUri: String, downloadId: Long, bookUrl: String):Boolean
}