package com.example.vbook.presentation.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vbook.presentation.LocalAppBarVM
import com.example.vbook.presentation.LocalNavController
import com.example.vbook.presentation.VBookScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun VBookTopAppBar(scaffoldState: ScaffoldState) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val screens = VBookScreen.values()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val route = navBackStackEntry.value?.destination?.route ?: "@"
    val currentScreenTitle =
        screens.asSequence().firstOrNull { route.startsWith(it.name) }?.title ?: ""
    AppropriateAppBar(
        scaffoldState,
        scope,
        currentScreenTitle
    )
}

@Composable
fun AppropriateAppBar(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    title: String,
) {
    val localAppBarVm = LocalAppBarVM.current
    val appBarType = localAppBarVm.currentType.collectAsState()
    when (appBarType.value) {
        AppBarVM.Type.Default -> {
            DefaultAppBar(scaffoldState, scope, title)
        }
        AppBarVM.Type.Search ->{
            DefaultAppBar(scaffoldState,scope,title){
                val isExpanded = localAppBarVm.isSearchBarOpened.collectAsState()
                val text = localAppBarVm.searchTextState.collectAsState()
                if (isExpanded.value){
                    SearchBar(
                        text = text.value ,
                        onTextChange = {localAppBarVm.updateSearchBarText(it)} ,
                        onCloseClicked = localAppBarVm.onClose,
                        onSearchClicked = localAppBarVm.onSearch
                    )
                }else{
                    IconButton(onClick = { localAppBarVm.isSearchBarOpened.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DefaultAppBar(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    title: String,
    content: @Composable RowScope.()->Unit = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
            ) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = ""
                )
            }
        },
        actions =  content
    )
}


@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    TextField(modifier = Modifier
        .fillMaxWidth(),
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                text = "Search here...",
                color = Color.White
            )
        },
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.White
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    if (text.isNotEmpty()) {
                        onTextChange("")
                    } else {
                        onCloseClicked()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon",
                    tint = Color.White
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClicked(text)
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
        ))
}