package com.example.vbook.presentation.ui.newbooks

import android.util.Log
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
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
    val booksState = vm.booksState.collectAsState()

    LaunchedEffect(vm) {
        Log.e("Book", "Launch-NB")
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
                        VBookScreen.SearchedBooks.name.addArgs(
                            "query",
                            searchTextState.value
                        )
                    )
                }
            )
        }
    }
    NewBooksBody(
        isRefreshing = isRefreshing.value,
        swipeEnabled = swipeEnabled.value ,
        onRefresh = vm::refresh,
        onAddMore = vm::loadMoreNewBooks,
        onItemClick = { bookUrl ->
            navController.navigate(
                VBookScreen.BookDetailed.name.addArgs(
                    "bookUrl",
                    bookUrl
                )
            )
        },
        booksState = booksState.value
    )
}

@Composable
fun NewBooksBody(
    isRefreshing: Boolean,
    swipeEnabled: Boolean,
    onRefresh: () -> Unit,
    onAddMore: () -> Unit,
    onItemClick: (String) -> Unit,
    booksState: ResourceState<List<Book>>
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh,
        swipeEnabled = swipeEnabled
    ) {
        StateSection(state = booksState) { books ->
            BookList(
                books = books,
                onItemClick = onItemClick,
                onAddMore = onAddMore
            )
        }

    }
}