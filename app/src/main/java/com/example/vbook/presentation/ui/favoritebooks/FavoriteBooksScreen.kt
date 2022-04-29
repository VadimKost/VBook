package com.example.vbook.presentation.ui.favoritebooks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.presentation.LocalAppBarVM
import com.example.vbook.presentation.LocalNavController
import com.example.vbook.presentation.VBookScreen
import com.example.vbook.presentation.addArgs
import com.example.vbook.presentation.components.StateSection
import com.example.vbook.presentation.components.appbar.AppBarVM


@Composable
fun FavoriteBooksScreen(
    vm: FavoriteBooksVM
) {
    val appBarVM = LocalAppBarVM.current
    val navController = LocalNavController.current

    val booksState = vm.booksState.collectAsState()

    LaunchedEffect(vm) {
        appBarVM.clearSearchBarCallBacks()
        appBarVM.apply {
            setType(AppBarVM.Type.Search)
            setSearchBarCallBacks(
                onCloseCallback = {
                    isSearchBarOpened.value = false
                },
                onSearchCallback = {
                    navController.popBackStack()
                    navController.navigate(
                        VBookScreen.SearchedBooks.name.addArgs(
                            "query",
                            searchTextState.value
                        )
                    )
                }
            )
        }
    }
    vm.getFavoriteBooks()

    FavoriteBooksBody(
        booksState = booksState.value,
        onItemClick = { bookUrl ->
            navController.navigate(
                VBookScreen.BookDetailed.name.addArgs("bookUrl", bookUrl)
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteBooksBody(
    booksState: ResourceState<List<Book>>,
    onItemClick: (String) -> Unit
) {
    StateSection(state = booksState) { books ->
        LazyVerticalGrid(cells = GridCells.Fixed(2)) {
            items(books) { book ->
                BookCard(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .height((256+64).dp)
                        .clickable { onItemClick(book.bookURL) }, book = book
                )
            }
        }
    }

}

@Composable
fun BookCard(
    modifier: Modifier,
    book: Book
) {
    Card(modifier, border = BorderStroke(color = Color.Black, width = 2.dp)) {
        Column() {
            Box(
                modifier = Modifier
                    .height(256.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = book.coverURL,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxHeight()
                )
            }

            Text(
                text = book.title,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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