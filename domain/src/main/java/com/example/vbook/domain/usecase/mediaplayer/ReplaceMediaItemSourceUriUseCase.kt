/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.usecase.mediaplayer

//@Singleton
//class ReplaceMediaItemSourceUriUseCase @Inject constructor(
//    val player: AudioBookPlayerService,
//    val getPlayerStateUseCase: GetPlayerStateUseCase
//) {
//    suspend operator fun invoke(localBook: Book, index: Int, newUri: String): Boolean {
//        var replaced = false
//        val state = getPlayerStateUseCase().value
//        if(state is AudioBookPlayerService.State.Ready){
//            if (state.playerMetadata.book == localBook){
//                replaced = player.replaceBookMediaItemUriSource(localBook, index, newUri)
//            }
//        }
//        return replaced
//    }
//
//}