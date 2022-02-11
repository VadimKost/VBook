package com.example.vbook.data.mapper

import android.app.DownloadManager
import com.example.vbook.data.db.model.MediaItemDownloadEntity
import com.example.vbook.domain.model.MediaItemDownload

fun MediaItemDownload.toData() = MediaItemDownloadEntity(
    mediaOnlineUri, downloadId, bookUrl
)

fun List<MediaItemDownload>.toData() = this.map { it.toData() }

fun MediaItemDownloadEntity.toDomain() = MediaItemDownload(
    mediaOnlineUri, downloadId, bookUrl
)

fun List<MediaItemDownloadEntity>.toDomain() = this.map { it.toDomain() }

fun Int?.mapDownloadStatusToDomain() = when(this){
    DownloadManager.STATUS_FAILED -> MediaItemDownload.Status.Failed
    DownloadManager.STATUS_PAUSED -> MediaItemDownload.Status.Paused
    DownloadManager.STATUS_PENDING -> MediaItemDownload.Status.Pending
    DownloadManager.STATUS_RUNNING -> MediaItemDownload.Status.Running
    DownloadManager.STATUS_SUCCESSFUL -> MediaItemDownload.Status.Successful
    null -> MediaItemDownload.Status.NotExists
    else -> MediaItemDownload.Status.Failed

}