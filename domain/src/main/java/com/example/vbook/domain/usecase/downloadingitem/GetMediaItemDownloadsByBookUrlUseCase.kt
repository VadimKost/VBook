/*
 * Created by Vadim on 16.07.22, 02:02
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 02:02
 *
 */

package com.example.vbook.domain.usecase.downloadingitem

import com.example.vbook.domain.repository.DownloadingItemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMediaItemDownloadsByBookUrlUseCase @Inject constructor(
    val downloadingItemRepository: DownloadingItemRepository,
) {
    suspend operator fun invoke(url:String) =
        downloadingItemRepository.getMediaItemDownloadsByBookUrl(url)
}
