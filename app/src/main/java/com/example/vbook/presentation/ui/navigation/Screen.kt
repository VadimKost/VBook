/*
 * Created by vadim on 03.04.25, 17:35
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:35
 *
 */

package com.example.vbook.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object New : Screen

    @Serializable
    data object Favorite : Screen

    @Serializable
    data class Detailed(val inAppBookId: String) : Screen

    @Serializable
    data object Search : Screen
}

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