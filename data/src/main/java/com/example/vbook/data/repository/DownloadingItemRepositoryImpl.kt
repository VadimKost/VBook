/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 *//*


package com.example.vbook.data.repository

import com.example.vbook.domain.downloader.port.DownloadRepository
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.DownloadingItem
import toData
import toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadingItemRepositoryImpl @Inject constructor(
    val db: AppDatabase,
) : DownloadRepository {

    override suspend fun addDownloadingItem(bookUrl: String, downloadId: Long, mediaUri:String) {
        val download = DownloadingItem(downloadId,mediaUri,bookUrl)
        return db.mediaItemDownloadDAO().insert(download.toData())
    }

    override suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<DownloadingItem>> {
        val downloadEntities = db.mediaItemDownloadDAO().getDownloadsByBookUri(url)
        if (downloadEntities.isEmpty()) return ResourceState.Empty
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

    override suspend fun getMediaItemDownloadByOnlineUri(uri: String): ResourceState<DownloadingItem> {
        val download = db.mediaItemDownloadDAO().getDownloadByOnlineUri(uri)
        return if (download != null) {
            ResourceState.Success(download.toDomain())
        } else {
            ResourceState.Empty
        }
    }


}*/
