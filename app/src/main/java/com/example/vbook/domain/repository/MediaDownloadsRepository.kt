package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.MediaItemDownload

interface MediaDownloadsRepository {

    suspend fun getMediaUri(uri: String):Result<String>
    suspend fun getMediaItemDownloadsByBook(url:String):Result<List<MediaItemDownload>>

}