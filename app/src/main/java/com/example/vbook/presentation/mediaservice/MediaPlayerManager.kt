package com.example.vbook.presentation.mediaservice

import android.net.Uri
import android.util.Log
import com.example.vbook.domain.model.Book
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class MediaPlayerManager(
    private val player: ExoPlayer,
    private val serviceCoroutineScope: CoroutineScope,
    val onBookUpdate: suspend (Book) -> Unit,
    playerListener: Player.Listener
) {

    init {
        player.prepare()
        player.addListener(playerListener)
    }

    private val _isPlaying = MutableStateFlow(false)
    private val _hasNext = MutableStateFlow(false)
    private val _trackIndex = MutableStateFlow(0)
    private val _trackTime = flow {
        while (true) {
            emit(player.currentPosition to player.duration)
            delay(1000L)
        }
    }

    val playbackInfo = combine(
        _isPlaying,
        _hasNext,
        _trackIndex,
        _trackTime
    ) { isPlaying, hasNext, trackIndex, trackTime ->
        PlaybackInfo(isPlaying,hasNext,trackIndex,trackTime)
    }

    fun saveBookStoppedIndexAndTime(book: Book?) {
        serviceCoroutineScope.launch {
            if (book != null) {
                withContext(Dispatchers.Main) {
                    Log.d(
                        "BookSet",
                        "${book.title} i= ${player.currentMediaItemIndex} t = ${player.contentPosition}"
                    )
                    book.stoppedTrackIndex = player.currentMediaItemIndex
                    book.stoppedTrackTime = player.currentPosition
                }
                withContext(Dispatchers.IO) {
                    onBookUpdate(book)
                }
            }
        }

    }

    fun updatePlayListStateInfo() {
        _trackIndex.value = player.currentWindowIndex
        _isPlaying.value = player.playWhenReady
        _hasNext.value = player.hasNextMediaItem()
    }

    fun preparePlayListForPlayer(book: Book, old: Book?) {
        if (old != null) {
            saveBookStoppedIndexAndTime(old)
            Log.d(
                "BookProbable",
                "${old.title} i= ${player.currentMediaItemIndex} t = ${player.contentPosition}"
            )
        }

        player.clearMediaItems()
        val initialWindowIndex = book.stoppedTrackIndex
        val playbackStartPositionMs = book.stoppedTrackTime

        player.addMediaItems(getMediaItemList(book))
        player.seekTo(initialWindowIndex, playbackStartPositionMs)
        updatePlayListStateInfo()
    }

    private fun getMediaItemList(book: Book): List<MediaItem> {
        val mediaItemList = mutableListOf<MediaItem>()
        book.mp3List!!.forEachIndexed { index, track ->
            mediaItemList.add(
                MediaItem.Builder()
                    .setUri(track.second)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(book.title)
                            .setArtist(book.author.first)
                            .setTrackNumber(index)
                            .setArtworkUri(Uri.parse(book.coverURL))
                            .build()
                    )
                    .build()
            )
        }
        return mediaItemList
    }

    data class PlaybackInfo(
        val isPlaying: Boolean=false,
        val hasNext: Boolean=false,
        val trackIndex: Int=0,
        val trackTime: Pair<Long, Long> = 0L to 0L
    )
}