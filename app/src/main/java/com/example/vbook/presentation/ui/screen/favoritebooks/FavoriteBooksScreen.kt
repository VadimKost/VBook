/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

//package com.example.vbook.presentation.ui.screen.favoritebooks
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Mic
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import coil.compose.rememberImagePainter
//import com.example.vbook.domain.model.Book
//import androidx.compose.runtime.getValue
//import com.example.vbook.presentation.ui.screen.newbooks.NewBooksUIEvent
//
//
//@Composable
//fun FavoriteBooksScreen(onBookClick: (Book) -> Unit) {
//
//    val vm: FavoriteBooksVM = hiltViewModel()
//    val state by vm.state.collectAsStateWithLifecycle()
//
//    Modifier.offset()
////    LaunchedEffect(vm) {
////        appBarVM.clearCallBacks()
////        appBarVM.apply {
////            setType(AppBarVM.Type.Search)
////            setSearchBarCallBacks(
////                onCloseCallback = {
////                    isSearchBarOpened.value = false
////                },
////                onSearchCallback = {
////                    navController.popBackStack()
////                    navController.navigate(
////                        VBookScreen.SearchedBooks.name.addArgs(
////                            "query",
////                            searchTextState.value
////                        )
////                    )
////                }
////            )
////        }
////    }
//
//    FavoriteBooksScreen(
//        state = state,
//        onEvent = { event ->
//            when (event) {
//                is NewBooksUIEvent.OnBookClick -> {
//                    onBookClick(event.book)
//                }
//
//                else -> {}
//            }
//        }
//    )
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun FavoriteBooksScreen(
//    state: FavoriteBooksUIState,
//    onEvent: (NewBooksUIEvent) -> Unit
//) {
//    if (state.books.isNotEmpty()) {
//        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
//            items(state.books) { book ->
//                BookCard(
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .fillMaxWidth()
//                        .height((256 + 64).dp)
//                        .clickable { onEvent(NewBooksUIEvent.OnBookClick(book)) },
//                    book = book
//                )
//            }
//        }
//    }
//
//}
//
//@Composable
//fun BookCard(
//    modifier: Modifier,
//    book: Book
//) {
//    Card(modifier, border = BorderStroke(color = Color.Black, width = 2.dp)) {
//        Column() {
//            Box(
//                modifier = Modifier
//                    .height(256.dp)
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = rememberImagePainter(
//                        data = book.coverUrl,
//                        builder = {
//                            crossfade(true)
//                        }
//                    ),
//                    contentDescription = null,
//                    contentScale = ContentScale.FillHeight,
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }
//
//            Text(
//                text = book.title,
//                modifier = Modifier
//                    .fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//            Row {
//                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
//                Text(
//                    text = book.author.first,
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//            }
//            Row {
//                Icon(imageVector = Icons.Filled.Mic, contentDescription = null)
//                Text(
//                    text = book.reader.first,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}