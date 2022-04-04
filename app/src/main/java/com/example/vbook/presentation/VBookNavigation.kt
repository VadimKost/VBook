package com.example.vbook.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.vbook.presentation.ui.bookdetailed.BookDetailedScreen
import com.example.vbook.presentation.ui.bookdetailed.BookDetailedVM
import com.example.vbook.presentation.ui.newbooks.NewBooksScreen
import com.example.vbook.presentation.ui.newbooks.NewBooksVM

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
            NewBooksScreen(vm, navController)
        }
        composable(
            VBookScreen.BookDetailed.name.addPathArgs("bookUrl"),
            arguments = listOf(navArgument("bookUrl") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            val vm = hiltViewModel<BookDetailedVM>()
            val bookUrl = it.arguments?.getString("bookUrl")
            BookDetailedScreen(vm, bookUrl!!, navController)
        }
    }
}

