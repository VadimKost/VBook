/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

//package com.example.vbook.presentation.ui.screen.searchedbook
//
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.vbook.domain.ResourceState
//import com.example.vbook.domain.model.Book
//import com.example.vbook.presentation.ui.navigation.VBookScreen
//import com.example.vbook.presentation.ui.navigation.addArgs
//import com.example.vbook.presentation.components.BookList
//import com.example.vbook.presentation.components.StateSection
//import com.example.vbook.presentation.components.appbar.AppBarVM
//import com.google.accompanist.swiperefresh.SwipeRefresh
//import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
//
//@Composable
//fun SearchedBooksScreen(navController:NavController) {
//    val vm: SearchedBookVM = hiltViewModel()
//    val isRefreshing = vm.isRefreshing.collectAsState()
//    val swipeEnabled = vm.canBeRefreshed.collectAsState()
//
//    val booksState = vm.booksState.collectAsState()
//
////    LaunchedEffect(vm) {
////        Log.e("Book","Launch-NB")
////        appBarVM.clearCallBacks()
////        appBarVM.apply {
////            setType(AppBarVM.Type.Search)
////            setSearchBarCallBacks(
////                onCloseCallback = {
////                    isSearchBarOpened.value = false
////                },
////                onSearchCallback = {
////                    navController.popBackStack()
////                    navController.navigate(
////                        VBookScreen.SearchedBooks.name.addArgs(
////                            "query",
////                            searchTextState.value
////                        )
////                    )
////                }
////            )
////        }
////    }
//    SearchedBookBody(
//        isRefreshing = isRefreshing.value,
//        swipeEnabled = swipeEnabled.value ,
//        onRefresh = { vm.refresh("query") },
//        onAddMore = { vm.loadMoreNewBooks("query") },
//        onItemClick = { bookUrl ->
//            navController.navigate(
//                VBookScreen.BookDetailed
//            )
//        },
//        booksState = booksState.value
//    )
//
//}
//
//@Composable
//fun SearchedBookBody(
//    isRefreshing: Boolean,
//    swipeEnabled: Boolean,
//    onRefresh: () -> Unit,
//    onAddMore: () -> Unit,
//    onItemClick: (Book) -> Unit,
//    booksState: ResourceState<List<Book>>
//) {
//    SwipeRefresh(
//        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
//        onRefresh = onRefresh,
//        swipeEnabled = swipeEnabled
//    ) {
//        StateSection(state = booksState) { books ->
//            BookList(
//                books = books,
//                onBookClick = onItemClick,
//                onAddMore = onAddMore
//            )
//        }
//
//    }
//}