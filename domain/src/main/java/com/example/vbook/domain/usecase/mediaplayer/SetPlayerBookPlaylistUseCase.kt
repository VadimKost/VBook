/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.usecase.mediaplayer

//@Singleton
//class SetPlayerBookPlaylistUseCase @Inject constructor(
//    val getPlayerStateUseCase: GetPlayerStateUseCase,
//    val player: AudioBookPlayerService
//) {
//    suspend operator fun invoke(book: Book) {
//        val state = getPlayerStateUseCase().value
//        if (state is AudioBookPlayerService.State.Ready || state is AudioBookPlayerService.State.WaitingForBook)
//            player.setCurrentBook(book)
//    }
//}