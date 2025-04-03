/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.usecase.mediaplayer

//@Singleton
//class PlaybackControlUseCase @Inject constructor(
//    val getPlayerStateUseCase: GetPlayerStateUseCase,
//    val player: AudioBookPlayerService
//) {
//    operator fun invoke(command: PlaybackControlCommand) {
//        val state = getPlayerStateUseCase().value
//        if (state is AudioBookPlayerService.State.Ready) {
//            when (command) {
//                is PlaybackControlCommand.Play -> player.play()
//                is PlaybackControlCommand.Pause -> player.pause()
//
//                is PlaybackControlCommand.Next -> player.next()
//                is PlaybackControlCommand.Previous -> player.previous()
//
//                is PlaybackControlCommand.Rewind -> player.rewind()
//                is PlaybackControlCommand.FastForward -> player.fastForward()
//
//                is PlaybackControlCommand.SeekTo -> player.seekTo(command.index, command.time)
//            }
//        }
//    }
//
//    sealed class PlaybackControlCommand {
//        data object Play : PlaybackControlCommand()
//        data object Pause : PlaybackControlCommand()
//
//        data object Next : PlaybackControlCommand()
//        data object Previous : PlaybackControlCommand()
//
//        data object Rewind : PlaybackControlCommand()
//        data object FastForward : PlaybackControlCommand()
//
//        data class SeekTo(val index: Int, val time: Long = 0) : PlaybackControlCommand()
//    }
//}

