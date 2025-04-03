/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.newbooks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vbook.presentation.components.BookList
import com.example.vbook.presentation.components.PullToRefreshBox
import androidx.compose.runtime.getValue
import com.example.vbook.domain.book.model.Book


//todo:Check recomposition counts
@Composable
fun NewBooksScreen(onBookClick: (Book) -> Unit) {
    val vm: NewBooksVM = hiltViewModel()
    val state by vm.state.collectAsStateWithLifecycle()

//    LaunchedEffect(vm) {
//        Log.e("Book", "Launch-NB")
//        appBarVM.clearCallBacks()
//        appBarVM.apply {
//            setType(AppBarVM.Type.Search)
//            setSearchBarCallBacks(onCloseCallback = {
//                isSearchBarOpened.value = false
//            }, onSearchCallback = {
//                navController.popBackStack()
//                navController.navigate(
//                    VBookScreen.SearchedBooks.name.addArgs(
//                        "query", searchTextState.value
//                    )
//                )
//            })
//        }
//    }

    NewBooksScreen(
        state = state,
        onEvent = { event ->
            vm.onEvent(event)
            when (event) {
                is NewBooksUIEvent.OnBookClick -> {
                    onBookClick(event.book)
                }

                else -> {}
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBooksScreen(
    state: NewBooksUiState,
    onEvent: (NewBooksUIEvent) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        enabled = state.errorMessage != null,
        onRefresh = {
            onEvent(NewBooksUIEvent.OnRefresh)
        },
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.errorMessage == null) {
            BookList(
                books = state.books,
                onBookClick = { book ->
                    onEvent(NewBooksUIEvent.OnBookClick(book))
                },
                onAddMore = {
                    onEvent(NewBooksUIEvent.OnAddMore)
                }
            )
        } else {
            //todo: Think is it good or not(probably not :D)
            Text(
                state.errorMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .wrapContentHeight()
            )
        }
    }
}