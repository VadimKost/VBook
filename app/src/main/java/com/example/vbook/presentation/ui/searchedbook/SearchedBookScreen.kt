package com.example.vbook.presentation.ui.searchedbook

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.presentation.LocalAppBarVM
import com.example.vbook.presentation.LocalNavController
import com.example.vbook.presentation.VBookScreen
import com.example.vbook.presentation.addArgs
import com.example.vbook.presentation.components.BookList
import com.example.vbook.presentation.components.StateSection
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

    val booksState = vm.booksState.collectAsState()

    LaunchedEffect(vm) {
        Log.e("Book","Launch-NB")
        vm.init(query)
        appBarVM.clearCallBacks()
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
    SearchedBookBody(
        isRefreshing = isRefreshing.value,
        swipeEnabled = swipeEnabled.value ,
        onRefresh = { vm.refresh(query) },
        onAddMore = { vm.loadMoreNewBooks(query) },
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
fun SearchedBookBody(
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