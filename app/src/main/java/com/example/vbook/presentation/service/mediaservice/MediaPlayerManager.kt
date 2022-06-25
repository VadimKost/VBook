package com.example.vbook.presentation.service.mediaservice

import android.net.Uri
import com.example.vbook.common.model.Book
import com.example.vbook.data.repository.mediaitem.DownloadingItemRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.EventListener

class MediaPlayerManager(
    private val player: ExoPlayer,
    playerListener:  Player.Listener,
    val downloadingItemRepository: DownloadingItemRepository
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
            delay(200L)
        }
    }

    val playbackInfo = combine(
        _isPlaying,
        _hasNext,
        _trackIndex,
        _trackTime
    ) { isPlaying, hasNext, trackIndex, trackTime ->
        PlaybackInfo(isPlaying, hasNext, trackIndex, trackTime)
    }

    fun updatePlaybackState() {
        _trackIndex.value = player.currentMediaItemIndex
        _isPlaying.value = player.playWhenReady
        _hasNext.value = player.hasNextMediaItem()
    }

    suspend fun preparePlayListForPlayer(book: Book) {
        player.clearMediaItems()
        val initialWindowIndex = book.stoppedTrackIndex
        val playbackStartPositionMs = book.stoppedTrackTime

        player.addMediaItems(getMediaItemList(book))
        player.seekTo(initialWindowIndex, playbackStartPositionMs)
        updatePlaybackState()
    }

    suspend fun replaceMediaItem(
        mediaUri: String,
        title: String,
        author: String,
        artworkUri: String,
        index: Int
    ) {
        val trackIndex = player.currentMediaItemIndex
        val position = player.currentPosition

        player.removeMediaItem(index)
        player.addMediaItem(index, getMediaItem(mediaUri, title, author, artworkUri, index))
        player.seekTo(trackIndex, position)
    }

    private suspend fun getMediaItemList(book: Book): List<MediaItem> {
        val mediaItemList = mutableListOf<MediaItem>()
        book.mediaItems!!.forEachIndexed { index, mediaItem ->
            mediaItemList.add(
                getMediaItem(
                    mediaItem.second,
                    mediaItem.first,
                    book.author.first,
                    book.coverURL,
                    index
                )
            )
        }
        return mediaItemList
    }

    private suspend fun getMediaItem(
        mediaUri: String,
        title: String,
        author: String,
        artworkUri: String,
        index: Int
    ): MediaItem {
        return MediaItem.Builder()
            .setUri(downloadingItemRepository.getAppropriateUri(mediaUri))
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(author)
                    .setTrackNumber(index)
                    .setArtworkUri(Uri.parse(artworkUri))
                    .build()
            )
            .build()
    }

    data class PlaybackInfo(
        val isPlaying: Boolean = false,
        val hasNext: Boolean = false,
        val trackIndex: Int = 0,
        val trackTime: Pair<Long, Long> = 0L to 0L
    )
}