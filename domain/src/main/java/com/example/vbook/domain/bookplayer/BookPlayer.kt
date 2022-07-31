/*
 * Created by Vadim on 10.07.22, 01:10
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 10.07.22, 01:10
 *
 */

package com.example.vbook.domain.bookplayer

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.player.PlayerMetadata
import kotlinx.coroutines.flow.StateFlow

typealias State<T> = ResourceState<T>
typealias IsConnecting = ResourceState.Loading
typealias WaitingForData = ResourceState.Empty
typealias TransferringData<T> = ResourceState.Success<T>

interface BookPlayer {
    fun getPlayerState(): StateFlow<State<PlayerMetadata>>
    fun play()
    fun pause()
    fun next()
    fun previous()
    fun fastForward()
    fun rewind()
    fun seekTo(index: Int, time: Long)
    suspend fun setPlayerBookPlaylist(book: Book): Boolean
    suspend fun replaceBookMediaItemUriSource(book: Book, index: Int, newUri: String): Boolean

}