package com.example.vbook.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vbook.presentation.common.UiState

@Composable
fun <T> StateSection(
    state: UiState<T>,
    modifier:Modifier=Modifier,
    errorMessage: String = "Oops! Something went wrong, Please try again after some time",
    emptyMessage: String = "Nothing in here Yet!, Please comeback later",
    content: @Composable (T) -> Unit)
{
    when (state) {
        is UiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

        }
        is UiState.Error -> {
            Text( text = errorMessage)
        }
        is UiState.Empty -> {

        }
        is UiState.Data -> {
            content(state.data)
        }

    }
}