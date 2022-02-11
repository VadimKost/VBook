package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.MediaItemDownload

interface MediaDownloadsRepository {

    suspend fun getMediaUri(uri: String):Result<String>

    suspend fun getMediaItemDownloadsByBookUrl(url:String):Result<List<MediaItemDownload>>
    suspend fun getMediaItemDownloadByDownloadId(downloadId:Long):Result<MediaItemDownload>

    suspend fun createMediaItemDownload(mediaOnlineUri: String, downloadId: Long, bookUrl: String):Boolean

}