/*
 * Created by Vadim on 16.07.22, 01:18
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 01:18
 *
 */

package com.example.vbook.domain.usecase.combination

//import com.example.vbook.domain.model.Book
//import com.example.vbook.domain.usecase.booktrakdownloader.StartDownloadingUseCase
//import com.example.vbook.domain.usecase.downloadingitem.CreateDownloadingItemUseCase
//import javax.inject.Inject
//import javax.inject.Singleton

//@Singleton
//class StartDownloadAndCreateDownloadingItemUseCase @Inject constructor(
//    val startDownloadingUseCase: StartDownloadingUseCase,
//    val createDownloadingItemUseCase: CreateDownloadingItemUseCase
//) {
//    suspend operator fun invoke(uri: String, book: Book) {
//        val downloadId = startDownloadingUseCase(uri, book.title)
//        createDownloadingItemUseCase(uri, downloadId, book)
//    }
//}