package com.example.vbook.data.repository

import com.example.vbook.data.db.AppDatabase
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.MediaItemDownload
import com.example.vbook.domain.repository.MediaDownloadsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDownloadsRepositoryImpl @Inject constructor(
    val db:AppDatabase
): MediaDownloadsRepository {
    override suspend fun getMediaUri(uri: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaItemDownloadsByBook(url: String): Result<List<MediaItemDownload>> {
        TODO("Not yet implemented")
    }

}