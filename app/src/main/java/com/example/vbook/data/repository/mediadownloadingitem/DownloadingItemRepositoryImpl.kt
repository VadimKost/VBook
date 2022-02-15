package com.example.vbook.data.repository.mediadownloadingitem

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.db.model.DownloadingItemEntity
import toDomain
import javax.inject.Inject

class DownloadingItemRepositoryImpl @Inject constructor(
    val db:AppDatabase
):DownloadingItemRepository{
    override suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<DownloadingItem>> {
        val downloadEntities = db.mediaItemDownloadDAO().getDownloadsByBookUri(url)
        return ResourceState.Success(downloadEntities.toDomain())
    }

    override suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): ResourceState<DownloadingItem> {
        val download = db.mediaItemDownloadDAO().getDownloadsByDownloadId(downloadId)
        return if (download != null) {
            ResourceState.Success(download.toDomain())
        } else {
            ResourceState.Empty
        }
    }

    override suspend fun createMediaItemDownload(
        mediaOnlineUri: String,
        downloadId: Long,
        bookUrl: String
    ): Boolean {
        val downloadEntity = DownloadingItemEntity(mediaOnlineUri, downloadId, bookUrl)
        db.mediaItemDownloadDAO().insert(downloadEntity)
        return true
    }
}