package com.example.vbook.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class VBookScreen(
    val icon: ImageVector,
) {
    NewBooks(
        icon = Icons.Filled.Search
    ),
    BookDetailed(
        icon = Icons.Filled.Search
    )
}

fun String.addArgs(name: String, value: String): String {
    return this.plus("?$name=$value&")
}

fun String.addPathArgs(key: String): String {
    return this.plus("?$key={$key}")
}