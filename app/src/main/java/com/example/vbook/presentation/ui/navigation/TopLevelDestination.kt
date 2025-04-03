/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vbook.R

enum class TopLevelDestination(val labelId: Int, val icon: ImageVector, val screen: Screen) {
    New(
        labelId = R.string.new_label,
        icon = Icons.Default.Home,
        screen = Screen.New
    ),
    Favorite(
        labelId = R.string.favorite,
        icon = Icons.Default.Favorite,
        screen = Screen.Favorite
    )
}