/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.application.usecase

import com.example.vbook.domain.player.port.MediaPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnPlaybackControlEventUseCase @Inject constructor() {

    operator fun invoke(playbackControlCommand: MediaPlayer.PlaybackControlCommand) {

    }

}