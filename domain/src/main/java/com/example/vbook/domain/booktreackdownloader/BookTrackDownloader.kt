package com.example.vbook.domain.booktreackdownloader

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.DownloadingItem
import kotlinx.coroutines.flow.StateFlow

interface BookTrackDownloader {
    fun startDownloading(uri: String, downloadingTitle: String): Long
    fun getDownloadStatus(downloadId: Long): DownloadingItem.Status
    fun getLocalUriByDownloadId(downloadId: Long): ResourceState<String>

    fun receiveDownloadedEvent(): StateFlow<ResourceState<Long>>
}