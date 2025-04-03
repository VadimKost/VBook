///*
// * Created by Vadim on 13.07.22, 12:15
// * Copyright (c) 2022 . All rights reserved.
// * Last modified 10.07.22, 01:50
// *
// */
//
//package com.example.vbook.device.service.mediaservice
//
//import android.content.Context
//import android.content.ServiceConnection
//import android.util.Log
//import com.example.vbook.device.bindService
//import com.example.vbook.domain.model.Book
//import com.example.vbook.domain.model.player.PlayerMetadata
//import com.example.vbook.domain.service.AudioBookPlayerService
//import com.google.android.exoplayer2.ExoPlayer
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.*
//import javax.inject.Inject
//
//
////Proxy that binds to real service and collect data when someone listen to its state
//
//class ForegroundAudioBookPlayerServiceServiceProxy @Inject constructor(
//    @ApplicationContext val context: Context
//) : AudioBookPlayerService {
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//
//    private val _playerState: MutableStateFlow<AudioBookPlayerService.State> =
//        MutableStateFlow(AudioBookPlayerService.State.Preparing)
//
//    private var _service: MediaService? = null
//    private var _serviceConnection: ServiceConnection? = null
//    var _player: ExoPlayer? = null
//
//    var bound = MutableStateFlow(false)
//
//    var emitJob: Job? = null
//
//    init {
//        scope.launch {
//            _playerState.subscriptionCount.collect {
//                if (it > 0 && !bound.value) {
//                    onBind()
//                    bound.value = true
//                    Log.d("Service", "bind")
//                    emitJob = scope.launch {
//                        emitPlayerState()
//                    }
//                } else if (it == 0) {
//                    bound.value = false
//                    emitJob?.cancel()
//                    onUnbind()
//                    Log.d("Service", "unbind")
//                }
//            }
//        }
//    }
//
//    private suspend fun emitPlayerState() {
//        val serviceBook = _service?.serviceBook
//        val servicePlaybackInfo = _service?.playbackInfo
//        _playerState.value = AudioBookPlayerService.State.Preparing
//        if (serviceBook != null && servicePlaybackInfo != null) {
//            combine(serviceBook, servicePlaybackInfo) { bookState, playbackInfoState ->
//                bookState.onSuccess { book ->
//                    playbackInfoState.onSuccess { playbackInfo ->
//                        return@combine AudioBookPlayerService.State.Ready(
//                            PlayerMetadata(
//                                playbackInfo, book
//                            )
//                        )
//                    }
//                }
//                return@combine AudioBookPlayerService.State.Preparing
//            }.collect {
//                _playerState.emit(it)
//            }
//        }
//    }
//
//    private suspend fun onBind() {
//        val (service, connection) = bindService(context)
//        _service = service
//        _serviceConnection = connection
//        _player = service.player
//
//    }
//
//    private fun onUnbind() {
//        _service = null
//        _serviceConnection?.let { context.unbindService(it) }
//        _serviceConnection = null
//        _player = null
//    }
//
//    override fun getPlayerState(): StateFlow<AudioBookPlayerService.State> {
//        return _playerState.asStateFlow()
//    }
//
//    override fun play() {
//        _player?.play()
//    }
//
//    override fun pause() {
//        _player?.pause()
//    }
//
//    override fun next() {
//        _player?.seekToNext()
//    }
//
//    override fun previous() {
//        _player?.seekToPrevious()
//    }
//
//    override fun fastForward() {
//        _player?.seekForward()
//    }
//
//    override fun rewind() {
//        _player?.seekBack()
//    }
//
//    override fun seekTo(index: Int, time: Long) {
//        _player?.seekTo(index, time)
//    }
//
//    //if book was set return true
//    override suspend fun setCurrentBook(book: Book): Boolean {
//        return _service?.setBook(book) ?: false
//    }
//
//    //if BookMediaItem was set return true
//    override suspend fun replaceBookMediaItemUriSource(
//        book: Book, index: Int, newUri: String
//    ): Boolean {
//        return _service?.replaceMediaItemUriSource(book, index, newUri) ?: false
//    }
//
//
//}