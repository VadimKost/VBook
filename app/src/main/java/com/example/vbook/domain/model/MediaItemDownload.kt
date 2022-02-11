package com.example.vbook.domain.model

data class MediaItemDownload(
    var mediaOnlineUri: String,
    var downloadId: Long,
    var bookUrl: String
) {

    enum class Status {
        Running, Pending, Paused, Successful, Failed, NotExists
    }
}

