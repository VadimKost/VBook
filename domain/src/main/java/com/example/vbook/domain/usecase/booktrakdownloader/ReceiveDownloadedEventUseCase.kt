/*
 * Created by Vadim on 22.07.22, 13:02
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 22.07.22, 13:02
 *
 */

package com.example.vbook.domain.usecase.booktrakdownloader

import com.example.vbook.domain.booktreackdownloader.BookTrackDownloader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiveDownloadedEventUseCase @Inject constructor(
    val bookTrackDownloader: BookTrackDownloader
) {
    operator fun invoke() = bookTrackDownloader.receiveDownloadedEvent()
}