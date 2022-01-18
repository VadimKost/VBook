package com.example.vbook.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.vbook.presentation.bookdetailed.BookDetailedScreen
import com.example.vbook.presentation.bookdetailed.BookDetailedVM
import com.example.vbook.presentation.newbooks.NewBooksVM
import com.example.vbook.presentation.newbooks.NewBooksScreen
import com.example.vbook.presentation.theme.VBookTheme
import dagger.hilt.android.AndroidEntryPoint

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
        val navController = rememberNavController()
        Scaffold { innerPadding ->
            VBookNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
            
        }
    }
}