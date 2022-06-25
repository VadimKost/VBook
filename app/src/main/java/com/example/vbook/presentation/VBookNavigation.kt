package com.example.vbook.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vbook.presentation.ui.bookdetailed.BookDetailedScreen
import com.example.vbook.presentation.ui.bookdetailed.BookDetailedVM
import com.example.vbook.presentation.ui.favoritebooks.FavoriteBooksScreen
import com.example.vbook.presentation.ui.newbooks.NewBooksScreen
import com.example.vbook.presentation.ui.newbooks.NewBooksVM
import com.example.vbook.presentation.ui.searchedbook.SearchedBooksScreen


@Composable
fun VBookNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = VBookScreen.NewBooks.name,
        modifier = modifier
    ) {
        composable(VBookScreen.NewBooks.name) {
            val vm = hiltViewModel<NewBooksVM>()
            NewBooksScreen(vm)
        }
        composable(
            VBookScreen.BookDetailed.name.addRouteArgs("bookUrl"),
            arguments = listOf(navArgument("bookUrl") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val vm = hiltViewModel<BookDetailedVM>()
            val bookUrl = it.arguments?.getString("bookUrl")
            val appBarVM = LocalAppBarVM.current
            BookDetailedScreen(vm, bookUrl!!)
        }
        composable(
            VBookScreen.SearchedBooks.name.addRouteArgs("query"),
            arguments = listOf(navArgument("bookUrl"){
                type = NavType.StringType
                nullable = true
            })
        ){
            val query = it.arguments?.getString("query")
            SearchedBooksScreen(vm = hiltViewModel(), query = query!!)
        }
        composable(VBookScreen.FavoriteBooks.name){
            FavoriteBooksScreen(vm = hiltViewModel())
        }
    }
}

