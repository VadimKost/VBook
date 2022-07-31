package com.example.vbook.domain.model

data class Book(
    var source: String = "",
    var bookUrl: String = "",
    var title: String = "",
    var author: Pair<String, String> = "" to "",
    var reader: Pair<String, String> = "" to "",
    var coverURL: String = "",
    var mediaItems: List<Pair<String, String>>? = null,
    val cycle: Pair<String, String> = "" to "",
    var cycleBookList: List<Pair<String, String>>? = null,
    var duration: String? = null,

    var isFavorite: Boolean = false,

    var stoppedTrackIndex: Int = 0,
    var stoppedTrackTime: Long = 0,
) {
    fun isDetailed(): Boolean {
        return mediaItems != null
    }
}

//Pair( title url)