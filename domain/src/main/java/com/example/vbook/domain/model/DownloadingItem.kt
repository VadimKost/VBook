package com.example.vbook.domain.model


data class DownloadingItem(
    var downloadId:Long,
    var onlineUri:String,
    var bookUrl:String
){
    enum class Status{
        DOWNLOADING,STOPPED,FAILED,SUCCESS,EMPTY
    }
}
