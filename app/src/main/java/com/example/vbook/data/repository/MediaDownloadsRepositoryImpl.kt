package com.example.vbook.data.repository

import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.mapper.toData
import com.example.vbook.data.mapper.toDomain
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.MediaItemDownload
import com.example.vbook.domain.repository.MediaDownloadsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDownloadsRepositoryImpl @Inject constructor(
    val db: AppDatabase,
) : MediaDownloadsRepository {

    override suspend fun getMediaUri(uri: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaItemDownloadsByBookUrl(url: String): Result<List<MediaItemDownload>> {
        val downloadsEntities = db.mediaItemDownloadDAO().getDownloadsByBookUrl(url)
        return Result.Success(downloadsEntities.toDomain())
    }

    override suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): Result<MediaItemDownload> {
        val download = db.mediaItemDownloadDAO().getDownloadsByDownloadId(downloadId)
        return if (download != null) {
            Result.Success(download.toDomain())
        } else {
            Result.Empty
        }
    }

    override suspend fun createMediaItemDownload(
        mediaOnlineUri: String,
        downloadId: Long,
        bookUrl: String
    ): Boolean {
        val downloadEntity = MediaItemDownload(mediaOnlineUri, downloadId, bookUrl)
        db.mediaItemDownloadDAO().insert(downloadEntity.toData())
        return true
    }


}