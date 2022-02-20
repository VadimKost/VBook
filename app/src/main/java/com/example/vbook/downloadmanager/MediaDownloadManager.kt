package com.example.vbook.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDownloadManager @Inject constructor(
    val downloadManager: DownloadManager,
    val downloadingItemRepository: DownloadingItemRepository,
    @ApplicationContext val context: Context
) {
    suspend fun initiateDownload(uri: String, book: Book): Long {
        val downloadRequest = createDownloadRequest(Uri.parse(uri), book.title)
        val downloadId = downloadManager.enqueue(downloadRequest)
        val download = DownloadingItem(uri, downloadId, book.bookURL)
        downloadingItemRepository.createDownloadingItem(download)
        return downloadId
    }

    private fun createDownloadRequest(uri: Uri, title: String) =
        DownloadManager.Request(uri)
            .setTitle(title)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalFilesDir(context, "", uri.path)

    fun getDownloadState(downloadId: Long): ResourceState<Unit> {
        val request = DownloadManager.Query().setFilterById(downloadId)
        val downloadingStatus = downloadManager.query(request).use {
            Log.e("Offline","cursor count ${it.count}")
            if (it.count > 0) {
                it.moveToFirst()
                val statusIndex = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
                it.getInt(statusIndex)
            } else {
                null
            }
        }
        return when (downloadingStatus) {
            null,
            DownloadManager.STATUS_FAILED ->
                ResourceState.Empty

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

    fun hasLocalCopy():Boolean{
        return false
    }
}