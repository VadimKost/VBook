/*
 * Created by vadim on 03.04.25, 17:35
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:35
 *
 */

package com.example.vbook.presentation.components.bootombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vbook.presentation.ui.navigation.TopLevelDestination
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController ) {
    NavigationBar{
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        TopLevelDestination.entries.forEach { topLevelRoute  ->
            NavigationBarItem(
                icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.screen::class)} == true,
                onClick = {
                    navController.navigate(topLevelRoute.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

