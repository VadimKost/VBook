package com.example.vbook.presentation.ui.searchedbook

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.navArgument
import com.example.vbook.presentation.LocalAppBarVM
import com.example.vbook.presentation.LocalNavController
import com.example.vbook.presentation.VBookScreen
import com.example.vbook.presentation.addArgs
import com.example.vbook.presentation.components.BookList
import com.example.vbook.presentation.components.appbar.AppBarVM
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SearchedBooksScreen(
    vm: SearchedBookVM = hiltViewModel(),
    query: String
) {
    val appBarVM = LocalAppBarVM.current
    val navController = LocalNavController.current
    val isRefreshing = vm.isRefreshing.collectAsState()
    val swipeEnabled = vm.canBeRefreshed.collectAsState()

    LaunchedEffect(vm) {
        Log.e("Book","Launch-NB")
        vm.init(query)
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
        onRefresh = { vm.refresh(query) },
        swipeEnabled = swipeEnabled.value
    ) {
        BookList(
            booksState = vm.booksState.collectAsState().value,
            onItemClick = { bookUrl ->
                navController.navigate(VBookScreen.BookDetailed.name.addArgs("bookUrl", bookUrl))
            },
            onAddMore = { vm.loadMoreNewBooks(query) }
        )
    }

}
