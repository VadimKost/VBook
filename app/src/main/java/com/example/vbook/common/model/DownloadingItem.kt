package com.example.vbook.common.model

data class DownloadingItem(
    var mediaOnlineUri:String,
    var downloadId:Long,
    var bookUrl:String
){
    enum class Status{
        DOWNLOADING,STOPPED,FAILED,SUCCESS,EMPTY
    }
}
