package com.example.vbook.presentation.ui.bookdetailed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.vbook.common.model.Book
import com.example.vbook.toTime
import com.example.vbook.presentation.components.StateSection
import com.example.vbook.service.mediaservice.MediaPlayerManager
import com.example.vbook.toSliderFloat

@Composable
fun BookDetailedScreen(
    vm: BookDetailedVM,
    navController: NavController
) {
    val bookState = vm.bookState.collectAsState()
    StateSection(state = bookState.value) { book ->
        val playbackInfo =
            vm.playbackMetadata.collectAsState(initial = MediaPlayerManager.PlaybackInfo()).value
        val player = vm.player

        BookDetailedBody(
            book = book,
            trackIndex = playbackInfo.trackIndex,
            trackTime = playbackInfo.trackTime,
            hasNext = playbackInfo.hasNext,
            isPlaying = playbackInfo.isPlaying,
            onPlay = player::play,
            onPause = player::pause,
            onRewind = player::seekBack,
            onForward = player::seekForward,
            onNext = player::seekToNextWindow,
            onPrevious = player::seekToPreviousWindow,
            onSeek = { player.seekTo(it) }
        )
    }


}


@Composable
fun BookDetailedBody(
    book: Book,
    trackIndex: Int,
    trackTime: Pair<Long, Long>,
    isPlaying: Boolean,
    hasNext: Boolean,
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onRewind: () -> Unit = {},
    onForward: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSeek: (Long) -> Unit
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                .fillMaxSize()
                .weight(3f)
        )
        Text(
            text = book.mp3List!![trackIndex].first +
                    "(${trackIndex + 1} из ${book.mp3List!!.size})",
            modifier = Modifier.weight(1f)
        )
        PlayerController(
            modifier = Modifier
                .weight(3f)
                .padding(8.dp),
            trackTime.first, trackTime.second, isPlaying,
            hasNext, onPlay, onPause, onRewind,
            onForward, onNext, onPrevious, onSeek
        )
    }
}

@Composable
fun PlayerController(
    modifier: Modifier = Modifier,
    trackTime: Long,
    duration: Long,
    isPlaying: Boolean,
    hasNext: Boolean,
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onRewind: () -> Unit = {},
    onForward: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSeek: (Long) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1.5f),
                text = trackTime.toTime(),
                style = MaterialTheme.typography.caption,
                maxLines = 1
            )
            Slider(
                modifier = Modifier.weight(8f),
                valueRange = 0f..100f,
                value = trackTime.toSliderFloat(duration),
                onValueChange = {
                    val time = ((duration.toFloat() * it) / 100).toLong()
                    onSeek(time)
                },
            )
            Text(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(start = 5.dp),
                text = duration.toTime(),
                style = MaterialTheme.typography.caption,
                maxLines = 1
            )
        }
        Row {
            MediaControlButton(size = 50.dp, padding = 2.dp, onClick = onPrevious) {
                Icon(Icons.Filled.SkipPrevious, null)
            }
            MediaControlButton(size = 50.dp, padding = 2.dp, onClick = onRewind) {
                Icon(Icons.Filled.FastRewind, null)
            }
            if (isPlaying) {
                MediaControlButton(size = 50.dp, padding = 2.dp, onClick = onPause) {
                    Icon(Icons.Filled.Pause, null)
                }
            } else {
                MediaControlButton(size = 50.dp, padding = 2.dp, onClick = onPlay) {
                    Icon(Icons.Filled.PlayArrow, null)
                }
            }
            MediaControlButton(size = 50.dp, padding = 2.dp, onClick = onForward) {
                Icon(Icons.Filled.FastForward, null)
            }
            MediaControlButton(
                size = 50.dp,
                padding = 2.dp,
                onClick = onNext,
                isVisible = hasNext
            ) {
                Icon(Icons.Filled.SkipNext, null)
            }


        }
    }


}

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