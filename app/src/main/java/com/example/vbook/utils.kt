package com.example.vbook

import com.example.vbook.domain.model.Book

fun Book.isDetailed(): Boolean {
    return mediaItems != null
}

fun Long.toTime(): String {
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    if (seconds < 10) {
        return "$minutes:0$seconds"
    }
    return "$minutes:$seconds"
}

fun Long.toSliderFloat(duration: Long): Float {
    if (this != 0L && duration != 0L) {
        return ((this * 100).toFloat()) / duration.toFloat()
    } else return 0f

}






