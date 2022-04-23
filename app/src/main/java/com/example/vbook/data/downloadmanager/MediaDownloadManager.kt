package com.example.vbook.data.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.repository.book.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDownloadManager @Inject constructor(
    val downloadManager: DownloadManager,
    val bookRepository: BookRepository,
    @ApplicationContext val context: Context
) {
    // TODO: Add download queue

    fun initiateDownload(uri: String, book: Book): Long {
        val downloadRequest = createDownloadRequest(Uri.parse(uri), book.title)
        val downloadId = downloadManager.enqueue(downloadRequest)
        return downloadId
    }

    private fun createDownloadRequest(uri: Uri, title: String) =
        DownloadManager.Request(uri)
            .setTitle(title)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalFilesDir(context, "", uri.path)
            .setVisibleInDownloadsUi(false)

    fun getDownloadStatus(downloadId: Long): DownloadingItem.Status {
        val request = DownloadManager.Query().setFilterById(downloadId)
        val downloadingStatus = downloadManager.query(request).use {
            if (it.count > 0) {
                it.moveToFirst()
                val statusIndex = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                it.getInt(statusIndex)
            } else {
                null
            }
        }
        return when (downloadingStatus) {
            null ->
                DownloadingItem.Status.EMPTY

            DownloadManager.STATUS_FAILED ->
                DownloadingItem.Status.FAILED

            DownloadManager.STATUS_RUNNING -> DownloadingItem.Status.DOWNLOADING

            DownloadManager.STATUS_PENDING,
            DownloadManager.STATUS_PAUSED ->
                DownloadingItem.Status.STOPPED

            DownloadManager.STATUS_SUCCESSFUL ->
                DownloadingItem.Status.SUCCESS

            else -> {
                DownloadingItem.Status.EMPTY
            }
        }
    }

    fun hasLocalCopy(downloadId: Long): Boolean {
        if (downloadManager.getUriForDownloadedFile(downloadId) != null) return true
        return false
    }

    fun getLocalUri(downloadId: Long): Uri {
        return downloadManager.getUriForDownloadedFile(downloadId)
    }
}