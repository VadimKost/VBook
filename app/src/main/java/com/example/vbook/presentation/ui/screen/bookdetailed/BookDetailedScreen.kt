/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.bookdetailed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.player.port.MediaPlayer
import com.example.vbook.domain.shared.isError
import com.example.vbook.domain.shared.isLoading
import com.example.vbook.domain.shared.isSuccess
import com.example.vbook.toSliderFloat
import com.example.vbook.toTime

@Composable
fun BookDetailedScreen(
    bookUrl: String,
) {
    val vm = hiltViewModel<BookDetailedVM, BookDetailedVM.Factory>(
        creationCallback = { factory ->
            factory.create(bookUrl)
        }
    )

//    val appBarVM = LocalAppBarVM.current
//    LaunchedEffect(bookUrl) {
//        appBarVM.clearCallBacks()
//        appBarVM.apply {
//            setType(AppBarVM.Type.Default)
//        }
//    }
    val state = vm.state.collectAsState().value

    BookDetailedBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is BookDetailedUIEvent.OnPlaybackControl -> vm.onPlaybackControlEvent(event.command)
//                is BookDetailedUIEvent.OnDownloadClick -> vm.onDownloadClick("", Book())
//                is BookDetailedUIEvent.OnFavoriteClick -> vm.onBookFavorite(event.isFavorite)
//                is BookDetailedUIEvent.OnDownloadDialogToggle -> vm.onDownloadDialogToggle(event.show)
                else -> {}
            }
        }
    )

}


@Composable
fun BookDetailedBody(
    state: BookDetailedUIState,
    onEvent: (BookDetailedUIEvent) -> Unit
) {

    val bookState = state.book
    val playerState = state.playerState


    if (bookState.isSuccess()) {

//        DownloadingDialog(
//            showDialog = state.isShowDownloadingDialog,
//            onClose = { onEvent(BookDetailedUIEvent.OnDownloadDialogToggle(false)) },
//            onDownloadClick = { _, _ -> onEvent(BookDetailedUIEvent.OnDownloadClick) },
//            book = bookState.data,
//            downloadsStatusState = state.downloads
//        )

        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = bookState.data.cover).apply {
                            crossfade(true)
                        }.build()
                ),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize()
                    .weight(1f)
            )

            Toolbar(
                modifier = Modifier.padding(16.dp),
                isBookFavorite = false,
                onEvent = onEvent
            )

            PlayerController(
                modifier = Modifier.padding(16.dp),
                playerState = playerState,
                book = bookState.data,
                onEvent = onEvent
            )

        }

    } else if (bookState.isError()) {
        Box(Modifier.fillMaxSize()) {
            Text(bookState.message)
        }
    } else if (bookState.isLoading()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }

}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    isBookFavorite: Boolean,
    onEvent: (BookDetailedUIEvent) -> Unit
) {
    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = if (isBookFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                Modifier.clickable { onEvent(BookDetailedUIEvent.OnFavoriteClick(!isBookFavorite)) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onEvent(
                        BookDetailedUIEvent.OnDownloadDialogToggle(false)
                    )
                }
            )
        }
    }
}

@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    playerState: MediaPlayer.State,
    book: Book,
    onEvent: (BookDetailedUIEvent) -> Unit,
) {
    //TODO do better
    var isChangingByUser by remember { mutableStateOf(false) }
    var userTime by remember { mutableLongStateOf(0L) }
    if (playerState is MediaPlayer.State.Ready) {
        val trackTime = playerState.playbackState.currentPosition
        val currentTime = if (isChangingByUser) userTime else trackTime

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = book.voiceovers.first().mediaItems[playerState.playbackState.currentTrackIndex].title +
                        "(${playerState.playbackState.currentTrackIndex + 1} из ${book.voiceovers.first().mediaItems.size})",
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1.5f),
                    text = currentTime.toTime(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
                Slider(
                    modifier = Modifier.weight(8f),
                    valueRange = 0f..100f,
                    value = currentTime.toSliderFloat(3000),
                    onValueChange = {
                        isChangingByUser = true
                        userTime = ((3000.toFloat() * it) / 100).toLong()
                    },
                    onValueChangeFinished = {
//                        onSeek(userTime)
                        isChangingByUser = false
                    }
                )
                Text(
                    modifier = Modifier
                        .weight(1.5f)
                        .padding(start = 5.dp),
                    text = 3000L.toTime(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
            Row {
                MediaControlButton(
                    size = 50.dp,
                    padding = 2.dp,
                    onClick = {
                        onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                    }
                ) {
                    Icon(Icons.Filled.SkipPrevious, null)
                }
                MediaControlButton(size = 50.dp, padding = 2.dp, onClick = {
                    onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                }) {
                    Icon(Icons.Filled.FastRewind, null)
                }
                if (playerState.playbackState.isPlaying) {
                    MediaControlButton(size = 50.dp, padding = 2.dp, onClick = {
                        onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                    }) {
                        Icon(Icons.Filled.Pause, null)
                    }
                } else {
                    MediaControlButton(size = 50.dp, padding = 2.dp, onClick = {
                        onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                    }) {
                        Icon(Icons.Filled.PlayArrow, null)
                    }
                }
                MediaControlButton(size = 50.dp, padding = 2.dp, onClick = {
                    onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                }) {
                    Icon(Icons.Filled.FastForward, null)
                }
                MediaControlButton(
                    size = 50.dp,
                    padding = 2.dp,
                    onClick = {
                        onEvent(BookDetailedUIEvent.OnPlaybackControl(MediaPlayer.PlaybackControlCommand.Previous))
                    },
                    isVisible = true
                ) {
                    Icon(Icons.Filled.SkipNext, null)
                }


            }
        }

    }


}

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun DownloadingDialog(
//    showDialog: Boolean,
//    downloadsStatusState: ResourceState<Map<String, DownloadingItem.Status>>,
//    onDownloadClick: (String, Book) -> Unit,
//    book: Book,
//    onClose: () -> Unit
//) {
//    if (showDialog) {
//        Dialog(
//            onDismissRequest = onClose,
//            properties = DialogProperties(
//                usePlatformDefaultWidth = false
//            )
//        ) {
//            Surface(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxSize(),
//                shape = RoundedCornerShape(16.dp),
//                color = Color(0xDCFFFFFF)
//            ) {
//                StateSection(state = downloadsStatusState) { downloadsStatus ->
//                    LazyColumn(Modifier.fillMaxSize()) {
//                        items(downloadsStatus.size) { index ->
//                            DownloadingItem(
//                                book = book,
//                                index = index,
//                                downloadsStatus = downloadsStatus,
//                                onDownloadClick = onDownloadClick
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DownloadingItem(
//    book: Book,
//    index: Int,
//    downloadsStatus: Map<String, DownloadingItem.Status>,
//    onDownloadClick: (String, Book) -> Unit,
//) {
//    val downloadingItemTitle = book.mediaItems!![index].first
//    val downloadingItemOnlineUri = book.mediaItems!![index].second
//    val status = downloadsStatus.getValue(downloadingItemOnlineUri)
//    Card(
//        Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        border = BorderStroke(1.dp, color = Color.Black)
//    ) {
//        Row(
//            Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(text = downloadingItemTitle, modifier = Modifier.weight(9f))
//            when (status) {
//                DownloadingItem.Status.EMPTY -> {
//                    Icon(
//                        imageVector = Icons.Default.DownloadForOffline,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clickable {
//                                onDownloadClick(book.mediaItems!![index].second, book)
//                            }
//                            .padding(4.dp)
//                    )
//                }
//
//                DownloadingItem.Status.DOWNLOADING -> {
//                    Icon(
//                        imageVector = Icons.Default.Downloading,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clickable {
//                                onDownloadClick(book.mediaItems!![index].second, book)
//                            }
//                            .padding(4.dp)
//                    )
//                }
//
//                DownloadingItem.Status.SUCCESS -> {
//                    Icon(
//                        imageVector = Icons.Default.DownloadDone,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .clickable {
//                                onDownloadClick(book.mediaItems!![index].second, book)
//                            }
//                            .padding(4.dp)
//                    )
//                }
//
//                else -> {}
//            }
//
//        }
//    }
//
//
//}

@Composable
fun MediaControlButton(
    size: Dp,
    padding: Dp,
    onClick: () -> Unit,
    isVisible: Boolean = true,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        OutlinedButton(
            modifier = Modifier
                .padding(padding)
                .size(size),
            shape = CircleShape,
            onClick = onClick
        ) {
            content()
        }
    } else {
        Box(
            modifier = Modifier
                .padding(padding)
                .size(size)
        )
    }
}
