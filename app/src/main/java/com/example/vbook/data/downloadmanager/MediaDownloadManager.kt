package com.example.vbook.data.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.repository.book.BookRepository
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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

    fun getDownloadStatus(downloadId: Long): ResourceState<Unit> {
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
                ResourceState.Empty

            DownloadManager.STATUS_FAILED ->
                ResourceState.Error("Failed")

            DownloadManager.STATUS_RUNNING,
            DownloadManager.STATUS_PENDING,
            DownloadManager.STATUS_PAUSED ->
                ResourceState.Loading

            DownloadManager.STATUS_SUCCESSFUL ->
                ResourceState.Success(Unit)

            else -> {
                ResourceState.Empty
            }
        }
    }

    fun hasLocalCopy(downloadId: Long):Boolean{
        if (downloadManager.getUriForDownloadedFile(downloadId) != null) return true
        return false
    }

    fun getLocalUri(downloadId: Long): Uri {
        return downloadManager.getUriForDownloadedFile(downloadId)
    }
}