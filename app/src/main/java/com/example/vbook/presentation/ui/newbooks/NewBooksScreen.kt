package com.example.vbook.presentation.ui.newbooks

import android.util.Log
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.vbook.common.model.Book
import com.example.vbook.presentation.VBookScreen
import com.example.vbook.presentation.addArgs
import com.example.vbook.common.ResourceState
import com.example.vbook.presentation.LocalAppBarVM
import com.example.vbook.presentation.LocalNavController
import com.example.vbook.presentation.components.BookList
import com.example.vbook.presentation.components.StateSection
import com.example.vbook.presentation.components.appbar.AppBarVM
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NewBooksScreen(
    vm: NewBooksVM = hiltViewModel(),
) {
    val appBarVM = LocalAppBarVM.current
    val navController = LocalNavController.current
    val isRefreshing = vm.isRefreshing.collectAsState()
    val swipeEnabled = vm.canBeRefreshed.collectAsState()

    LaunchedEffect(vm){
        Log.e("Book","Launch-NB")
        appBarVM.clearSearchBarCallBacks()
        appBarVM.apply {
            setType(AppBarVM.Type.Search)
            setSearchBarCallBacks(
                onCloseCallback = {
                    isSearchBarOpened.value = false
                },
                onSearchCallback = {
                    navController.popBackStack()
                    navController.navigate(
                        VBookScreen.SearchedBook.name.addArgs(
                            "query",
                            searchTextState.value
                        )
                    )
                }
            )
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = vm::refresh,
        swipeEnabled = swipeEnabled.value
    ) {
        BookList(
            booksState = vm.booksState.collectAsState().value,
            onItemClick = { bookUrl ->
                navController.navigate(VBookScreen.BookDetailed.name.addArgs("bookUrl", bookUrl))
            },
            onAddMore = vm::loadMoreNewBooks
        )
    }

}