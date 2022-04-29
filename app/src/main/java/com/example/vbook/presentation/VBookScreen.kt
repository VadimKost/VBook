package com.example.vbook.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class VBookScreen(
    val icon: ImageVector,
    val title: String,
) {
    NewBooks(
        icon = Icons.Filled.Search,
        title = "New books"
    ),
    BookDetailed(
        icon = Icons.Filled.Search,
        title = ""
    ),
    SearchedBooks(
    icon = Icons.Filled.Search,
    title = "Results"
    ),
    FavoriteBooks(
        icon = Icons.Filled.Favorite,
        title = "Favorite books"
    )
}

fun String.addArgs(name: String, value: String): String {
    return this.plus("?$name=$value&")
}

fun String.addRouteArgs(key: String): String {
    return this.plus("?$key={$key}")
}