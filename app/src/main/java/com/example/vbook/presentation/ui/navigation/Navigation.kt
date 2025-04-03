/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.vbook.presentation.ui.screen.bookdetailed.BookDetailedScreen
import com.example.vbook.presentation.ui.screen.newbooks.NewBooksScreen


@Composable
fun VBookNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.New,
        modifier = modifier
    ) {
        composable<Screen.New> {
            NewBooksScreen(
                onBookClick = { book ->
                    navController.navigate(Screen.Detailed(inAppBookId = book.inAppId))
                }
            )
        }

        composable<Screen.Detailed> { backStackEntry ->
            val bookUrl = backStackEntry.toRoute<Screen.Detailed>().inAppBookId
            BookDetailedScreen(bookUrl)
        }
//        composable<Screen.Search> { SearchedBooksScreen(navController) }
//
//        composable<Screen.Favorite> {
//            FavoriteBooksScreen(
//                onBookClick = { book ->
//                    navController.navigate(Screen.Detailed(bookUrl = book.bookUrl))
//                }
//            )
//        }
    }
}

