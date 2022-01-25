package com.example.vbook.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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