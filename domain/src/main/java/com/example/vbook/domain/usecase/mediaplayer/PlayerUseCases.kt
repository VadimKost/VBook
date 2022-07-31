/*
 * Created by Vadim on 31.07.22, 19:34
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 31.07.22, 19:34
 *
 */

package com.example.vbook.domain.usecase.mediaplayer

import com.example.vbook.domain.usecase.mediaplayer.playback_controll.*
import javax.inject.Inject

class PlayerUseCases @Inject constructor(
    val getPlayerStateUseCase: GetPlayerStateUseCase,

    val playUseCase: PlayUseCase,
    val pauseUseCase: PauseUseCase,
    val nextUseCase: NextUseCase,
    val previousUseCase: PreviousUseCase,
    val fastForwardUseCase: FastForwardUseCase,
    val rewindUseCase: RewindUseCase,
    val seekToUseCae: SeekToUseCae,

    val replaceMediaItemSourceUriUseCase: ReplaceMediaItemSourceUriUseCase,
    val setPlayerBookPlaylistUseCase: SetPlayerBookPlaylistUseCase
)