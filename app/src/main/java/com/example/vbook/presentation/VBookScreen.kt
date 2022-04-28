package com.example.vbook.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class VBookScreen(
    val icon: ImageVector,
    val title: String,
) {
    NewBooks(
        icon = Icons.Filled.Search,
        title = "New Books"
    ),
    BookDetailed(
        icon = Icons.Filled.Search,
        title = ""
    ),
    SearchedBook(
    icon = Icons.Filled.Search,
    title = "Results"
    )
}

fun String.addArgs(name: String, value: String): String {
    return this.plus("?$name=$value&")
}

fun String.addRouteArgs(key: String): String {
    return this.plus("?$key={$key}")
}