package com.example.vbook.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.vbook.domain.player.port.MediaPlayer
import com.example.vbook.presentation.components.bootombar.BottomBar
import com.example.vbook.presentation.theme.VBookTheme
import com.example.vbook.presentation.ui.navigation.VBookNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            player.observePlayerState().collectLatest {
                Log.e("asd",it.toString())
            }
        }
        setContent {
            VBookApp()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VBookApp() {
    VBookTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomBar(navController = navController) }
        ) { innerPadding ->
            VBookNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}