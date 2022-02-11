package com.example.vbook.presentation.ui.newbooks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.vbook.presentation.model.Book
import com.example.vbook.presentation.VBookScreen
import com.example.vbook.presentation.addArgs
import com.example.vbook.common.ResourceState
import com.example.vbook.presentation.components.StateSection
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NewBooksScreen(
    vm: NewBooksVM = hiltViewModel(),
    navController: NavController
) {
    val isRefreshing = vm.isRefreshing.collectAsState()
    val swipeEnabled = vm.canBeRefreshed.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = vm::refresh,
        swipeEnabled = swipeEnabled.value
    ) {
        NewBooksBody(
            booksState = vm.booksState.collectAsState().value,
            onItemClick = { bookUrl ->
                navController.navigate(VBookScreen.BookDetailed.name.addArgs("bookUrl", bookUrl))
            },
            onAddMore = vm::loadMoreNewBooks
        )
    }

}

@Composable
fun NewBooksBody(
    booksState: ResourceState<List<Book>>,
    onItemClick: (String) -> Unit = {},
    onAddMore: () -> Unit = {}
) {
    StateSection(state = booksState) { books ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(books) { book ->
                Card(
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(5.dp)
                        .clickable(onClick = { onItemClick(book.bookURL) })
                ) {
                    Row {
                        Image(
                            painter = rememberImagePainter(
                                data = book.coverURL,
                                builder = {
                                    crossfade(true)
                                },
                            ),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(140.dp)
                        )
                        Column {
                            Text(
                                text = book.title,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Row {
                                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                                Text(
                                    text = book.author.first,
                                    style = MaterialTheme.typography.body2,
                                )
                            }
                            Row {
                                Icon(imageVector = Icons.Filled.Mic, contentDescription = null)
                                Text(
                                    text = book.reader.first,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onAddMore,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "MORE")
                }
            }
        }
    }
}
