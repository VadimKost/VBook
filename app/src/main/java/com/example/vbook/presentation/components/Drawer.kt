package com.example.vbook.presentation.components

import android.icu.text.CaseMap
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vbook.presentation.VBookScreen
import kotlinx.coroutines.launch

@Composable
fun Drawer(navController: NavController, scaffoldState: ScaffoldState) {
    val screens = listOf(VBookScreen.NewBooks, VBookScreen.FavoriteBooks)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val coroutineScope = rememberCoroutineScope()


    Column(Modifier.fillMaxSize()) {
        Text(text = "VBook", style = MaterialTheme.typography.h4, modifier = Modifier.padding(8.dp))
        Divider(color = Color.White, thickness = 2.dp)
        screens.forEach { screen ->
            NavigationItem(
                onClick = {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                    navController.navigate(screen.name) {
                        launchSingleTop = true
                    }
                },
                isSelected = currentDestination?.route == screen.name,
                title = screen.title)
        }
    }

}

@Composable
fun NavigationItem(
    onClick: () -> Unit,
    isSelected: Boolean,
    title: String
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(32.dp)
            .clip(RoundedCornerShape(10))
            .clickable { onClick() }
            .border(
                BorderStroke(
                    color = if (isSelected) Color.Yellow else Color.White,
                    width = 2.dp
                )
            )
            .padding(start = 8.dp, top = 2.dp, bottom = 2.dp),
    ) {
        Text(
            text = title,
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}