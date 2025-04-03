/*
 * Created by Vadim on 21.07.22, 21:51
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 21.07.22, 21:45
 *
 */

package com.example.vbook.domain.usecase.combination

//import com.example.vbook.domain.ResourceState
//import com.example.vbook.domain.model.Book
//import com.example.vbook.domain.usecase.booktrakdownloader.GetLocalUriByDownloadIdUseCase
//import com.example.vbook.domain.usecase.downloadingitem.GetMediaItemDownloadByDownloadIdUseCase
//import com.example.vbook.domain.usecase.mediaplayer.ReplaceMediaItemSourceUriUseCase
//import javax.inject.Inject
//import javax.inject.Singleton
//
////TODO SPLIT FUNCTIONALITY? And make it without side effect
//@Singleton
//class OnDownloadCompleteUseCase @Inject constructor(
//    val getMediaItemDownloadByDownloadIdUseCase: GetMediaItemDownloadByDownloadIdUseCase,
//    val getLocalUriByDownloadIdUseCase: GetLocalUriByDownloadIdUseCase,
//    val replaceMediaItemSourceUriUseCase: ReplaceMediaItemSourceUriUseCase
//) {
//    suspend operator fun invoke(downloadId: Long, localBook: Book): ResourceState<String> {
//        val downloadingItem = getMediaItemDownloadByDownloadIdUseCase(downloadId).getOrNull()
//        val localUri = getLocalUriByDownloadIdUseCase(downloadId).getOrNull()
//        var successReplacing = false
//        if (downloadingItem != null && downloadingItem.bookUrl == localBook.bookUrl && localUri != null) {
//            val indexToReplace =
//                localBook.mediaItems!!.map { it.second }.indexOf(downloadingItem.onlineUri)
//            successReplacing = replaceMediaItemSourceUriUseCase(
//                localBook,
//                indexToReplace,
//                localUri
//            )
//            return ResourceState.Success(downloadingItem.onlineUri)
//        }
//        return ResourceState.Error("There is no downloadingItem with such id ")
//    }
//
//
////        if (downloadsState is ResourceState.Success) {
////            if (downloadingItem is ResourceState.Success) {
////                val mutableDownloadsState = downloadsState.data.toMutableMap()
////                mutableDownloadsState[downloadingItem.data.mediaOnlineUri] =
////                    getDownloadStatusUseCase(downloadingItem.data.downloadId)
////                setDownloadsStatus(mutableDownloadsState)
////                val bookState = serviceBook.value
////                if (bookState is ResourceState.Success) {
////                    val book = bookState.data
////                    val itemToReplace =
////                        book.mediaItems!!.first { downloadingItem.data.mediaOnlineUri == it.second }
////                    val indexToReplace = book.mediaItems!!.indexOf(itemToReplace)
////                    _serviceState.value.getOrNull()?.replaceMediaItem(
////                        itemToReplace.second,
////                        itemToReplace.first,
////                        book.author.first,
////                        book.coverURL,
////                        indexToReplace
////                    )
////                }
////
////            }
////        }
//
//}