package com.example.vbook.presentation.service.download

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.example.vbook.data.mapper.mapDownloadStatusToDomain
import com.example.vbook.data.mapper.toData
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.MediaItemDownload
import com.example.vbook.domain.usecases.CreateMediaItemDownloadUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadServiceManager @Inject constructor(
    val downloadManager: DownloadManager,
    @ApplicationContext val context: Context,
    val createMediaItem: CreateMediaItemDownloadUseCase
) {
    suspend fun initiateDownload(uri: String, book: Book): Result<Long> {
        val downloadRequest = createDownloadRequest(Uri.parse(uri), book.title)
        val downloadId = downloadManager.enqueue(downloadRequest)
        val download = MediaItemDownload(uri, downloadId, book.bookURL)
        createMediaItem(uri, downloadId,book.bookURL)
        return Result.Success(downloadId)
    }

    private fun createDownloadRequest(uri: Uri, title: String) =
        DownloadManager.Request(uri)
            .setTitle(title)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalFilesDir(context, "", uri.path)

    fun getDownloadStatus(downloadId: Long): MediaItemDownload.Status {
        val request = DownloadManager.Query().setFilterById(downloadId)
        downloadManager.query(request).use {
            return if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = it.getInt(columnIndex)
                return status.mapDownloadStatusToDomain()
            } else {
                MediaItemDownload.Status.NotExists
            }
        }
    }
}