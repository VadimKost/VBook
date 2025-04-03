/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.downloader.model


//data class DownloadingItem(
//    var downloadId:Long,
//    var onlineUri:String,
//    var bookUrl:String
//){
//    enum class Status{
//        DOWNLOADING,STOPPED,FAILED,SUCCESS,EMPTY
//    }
//}

class Download(
    val id: String,
    val downloadingItem: DownloadItem,
    private var progress: DownloadProgress = DownloadProgress(0, 0),
    private var status: DownloadStatus = DownloadStatus.PENDING
)

