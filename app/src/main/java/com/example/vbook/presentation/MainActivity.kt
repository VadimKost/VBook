package com.example.vbook.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vbook.presentation.theme.VBookTheme
import com.example.vbook.presentation.components.Drawer
import com.example.vbook.presentation.components.appbar.AppBarVM
import com.example.vbook.presentation.components.appbar.VBookTopAppBar
import dagger.hilt.android.AndroidEntryPoint

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController provided ") }
val LocalAppBarVM = compositionLocalOf<AppBarVM> { error("No NavController provided ") }

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VBookApp()
        }
    }

}

@Composable
fun VBookApp(){
    VBookTheme {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberNavController()
        val topAppBarVM:AppBarVM = hiltViewModel()
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalAppBarVM provides topAppBarVM
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = { VBookTopAppBar(scaffoldState) },
                drawerContent = { Drawer(navController,scaffoldState) },
                drawerBackgroundColor = MaterialTheme.colors.primaryVariant
            ) { innerPadding ->
                VBookNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )

            }
        }
    }
}