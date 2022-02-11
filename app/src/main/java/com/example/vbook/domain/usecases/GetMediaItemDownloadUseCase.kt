package com.example.vbook.domain.usecases

import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.MediaItemDownload
import com.example.vbook.domain.repository.MediaDownloadsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMediaItemDownloadUseCase @Inject constructor(
    val mediaDownloadsRepository: MediaDownloadsRepository
){
    suspend operator fun invoke(downloadId:Long): Result<MediaItemDownload> {
        return mediaDownloadsRepository.getMediaItemDownloadByDownloadId(downloadId)
    }
}