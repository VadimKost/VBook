/*
 * Created by Vadim on 16.07.22, 01:03
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 01:03
 *
 */

package com.example.vbook.domain.usecase.booktrakdownloader

import com.example.vbook.domain.booktreackdownloader.BookTrackDownloader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartDownloadingUseCase @Inject constructor(
    val bookTrackDownloader: BookTrackDownloader
) {
    operator fun invoke(uri: String, downloadingTitle: String) =
        bookTrackDownloader.startDownloading(uri, downloadingTitle)
}