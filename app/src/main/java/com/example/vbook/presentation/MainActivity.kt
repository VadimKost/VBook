package com.example.vbook.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vbook.presentation.components.BottomBar
import com.example.vbook.presentation.components.appbar.AppBarVM
import com.example.vbook.presentation.components.appbar.VBookTopAppBar
import com.example.vbook.presentation.theme.VBookTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided ") }
val LocalAppBarVM = compositionLocalOf<AppBarVM> { error("No NavController provided ") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VBookApp()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VBookApp(){
    VBookTheme {
        val navController = rememberNavController()
        val topAppBarVM:AppBarVM = hiltViewModel()
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalAppBarVM provides topAppBarVM
        ) {
            Scaffold(
                topBar = { VBookTopAppBar() },
                bottomBar = { BottomBar(navController = navController)}
            ) { innerPadding ->
                VBookNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )

            }
        }
    }
}