package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResult
import com.example.vbook.domain.model.MediaItemDownload
import com.example.vbook.domain.repository.MediaDownloadsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateMediaItemDownloadUseCase @Inject constructor(
    val mediaItemDownloadsRepository: MediaDownloadsRepository
) {
    suspend operator fun invoke(
        mediaOnlineUri: String,
        downloadId: Long,
        bookUrl: String
    ): Boolean =
        mediaItemDownloadsRepository.createMediaItemDownload(mediaOnlineUri, downloadId, bookUrl)
}