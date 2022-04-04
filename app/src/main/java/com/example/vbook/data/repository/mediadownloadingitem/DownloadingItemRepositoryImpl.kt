package com.example.vbook.data.repository.mediadownloadingitem

import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.db.AppDatabase
import com.example.vbook.data.db.model.DownloadingItemEntity
import com.example.vbook.data.downloadmanager.MediaDownloadManager
import toData
import toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadingItemRepositoryImpl @Inject constructor(
    val db: AppDatabase,
    val mediaDownloadManager: MediaDownloadManager
) : DownloadingItemRepository {

    override suspend fun createDownloadingItem(uri: String, book: Book) {
        val downloadId = mediaDownloadManager.initiateDownload(uri, book)
        val download = DownloadingItem(uri, downloadId, book.bookURL)
        return db.mediaItemDownloadDAO().insert(download.toData())
    }

    override fun getDownloadStatus(downloadId: Long): ResourceState<Unit> =
        mediaDownloadManager.getDownloadStatus(downloadId)

    override suspend fun getBookDownloadingItemsStatus(book: Book): Map<String, ResourceState<Unit>> {
        val mediaUrls = book.mediaItems!!.map { it.second }
        val status = mutableMapOf<String, ResourceState<Unit>>()
        val downloadingItems = getMediaItemDownloadsByBookUrl(book.bookURL)
        when (downloadingItems) {
            is ResourceState.Empty -> {
                mediaUrls.forEach { mediaUrl ->
                    status[mediaUrl] = ResourceState.Empty
                }
                return status
            }
            is ResourceState.Success -> {
                mediaUrls.forEach { mediaUrl ->
                    if (downloadingItems.data.any { mediaUrl == it.mediaOnlineUri }) {
                        val downloadingItem =
                            downloadingItems.data.first { it.mediaOnlineUri == mediaUrl }
                        if (mediaDownloadManager.hasLocalCopy(downloadingItem.downloadId)) {
                            status[mediaUrl] = ResourceState.Success(Unit)
                        } else {
                            val downloadingState = getDownloadStatus(downloadingItem.downloadId)
                            status[mediaUrl] = downloadingState
                        }
                    }else{
                        status[mediaUrl] = ResourceState.Empty
                    }

                }
                return status
            }
        }
        return status
    }

    override suspend fun getMediaItemDownloadsByBookUrl(url: String): ResourceState<List<DownloadingItem>> {
        val downloadEntities = db.mediaItemDownloadDAO().getDownloadsByBookUri(url)
        if (downloadEntities.isEmpty()) return ResourceState.Empty
        return ResourceState.Success(downloadEntities.toDomain())
    }

    override suspend fun getMediaItemDownloadByDownloadId(downloadId: Long): ResourceState<DownloadingItem> {
        val download = db.mediaItemDownloadDAO().getDownloadsByDownloadId(downloadId)
        return if (download != null) {
            ResourceState.Success(download.toDomain())
        } else {
            ResourceState.Empty
        }
    }

    override suspend fun getMediaItemDownloadByOnlineUri(uri: String): ResourceState<DownloadingItem> {
        val download = db.mediaItemDownloadDAO().getDownloadByUri(uri)
        return if (download != null) {
            ResourceState.Success(download.toDomain())
        } else {
            ResourceState.Empty
        }
    }

    override suspend fun getAppropriateUri(uri: String): String {
        val downloadItem = getMediaItemDownloadByOnlineUri(uri)
        return if (downloadItem is ResourceState.Success) {
            if (mediaDownloadManager.hasLocalCopy(downloadItem.data.downloadId)) {
                mediaDownloadManager.getLocalUri(
                    downloadItem.data.downloadId
                ).toString()
            } else downloadItem.data.mediaOnlineUri
        } else uri
    }
}