///*
// * Created by Vadim on 13.07.22, 12:14
// * Copyright (c) 2022 . All rights reserved.
// * Last modified 10.07.22, 01:14
// *
// */
//
//package com.example.vbook.device.service.mediaservice
//
//import android.net.Uri
//import com.example.vbook.domain.ResourceState
//import com.example.vbook.domain.model.Book
//import com.example.vbook.domain.model.player.PlaybackInfo
//import com.example.vbook.domain.usecase.combination.GetAppropriateUriUseCase
//import com.google.android.exoplayer2.ExoPlayer
//import com.google.android.exoplayer2.MediaItem
//import com.google.android.exoplayer2.MediaMetadata
//import com.google.android.exoplayer2.Player
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.*
//
//class MediaPlayerManager(
//    private val player: ExoPlayer,
//    playerListener: Player.Listener,
//    val getAppropriateUriUseCase: GetAppropriateUriUseCase,
//    scope: CoroutineScope
//) {
//
//    init {
//        player.prepare()
//        player.addListener(playerListener)
//    }
//
//    private val _isPlaying = MutableStateFlow(false)
//    private val _hasNext = MutableStateFlow(false)
//    private val _trackIndex = MutableStateFlow(0)
//    private val _trackTime = flow {
//        while (true) {
//            emit(player.currentPosition to player.duration)
//            delay(200L)
//        }
//    }
//
//    val playbackInfo = combine(
//        _isPlaying, _hasNext, _trackIndex, _trackTime
//    ) { isPlaying, hasNext, trackIndex, trackTime ->
//        if (player.mediaItemCount > 0) {
//            ResourceState.Success(PlaybackInfo(isPlaying, hasNext, trackIndex, trackTime))
//        } else {
//            ResourceState.Loading
//        }
//    }
//        .flowOn(Dispatchers.Main)
//        .stateIn(scope, SharingStarted.Lazily, ResourceState.Loading)
//
//    fun updatePlaybackState() {
//        _trackIndex.value = player.currentMediaItemIndex
//        _isPlaying.value = player.playWhenReady
//        _hasNext.value = player.hasNextMediaItem()
//    }
//
//    suspend fun preparePlayListForPlayer(book: Book) {
//        player.clearMediaItems()
//        val initialWindowIndex = book.stoppedTrackIndex
//        val playbackStartPositionMs = book.stoppedTrackTime
//
//        player.addMediaItems(getMediaItemList(book))
//        player.seekTo(initialWindowIndex, playbackStartPositionMs)
//        updatePlaybackState()
//    }
//
//    suspend fun replaceMediaItem(
//        book: Book, index: Int, newUri: String
//    ): Boolean {
//        val trackIndex = player.currentMediaItemIndex
//        val position = player.currentPosition
//        player.removeMediaItem(index)
//        player.addMediaItem(
//            index, getMediaItem(newUri, book.title, book.author.first, book.coverUrl, index)
//        )
//        player.seekTo(trackIndex, position)
//        return true
//    }
//
//    private suspend fun getMediaItemList(book: Book): List<MediaItem> {
//        val mediaItemList = mutableListOf<MediaItem>()
//        book.mediaItems!!.forEachIndexed { index, mediaItem ->
//            mediaItemList.add(
//                getMediaItem(
//                    mediaItem.second, mediaItem.first, book.author.first, book.coverUrl, index
//                )
//            )
//        }
//        return mediaItemList
//    }
//
//    private suspend fun getMediaItem(
//        mediaUri: String, title: String, author: String, artworkUri: String, index: Int
//    ): MediaItem {
//        getAppropriateUriUseCase(mediaUri).onSuccess {
//            return MediaItem.Builder().setUri(it).setMediaMetadata(
//                MediaMetadata.Builder().setTitle(title).setArtist(author).setTrackNumber(index)
//                    .setArtworkUri(Uri.parse(artworkUri)).build()
//            ).build()
//        }
//        throw IllegalStateException("Has no appropriateUri")
//    }
//}