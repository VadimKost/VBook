package com.example.vbook.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun VBookTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
