/*
 * Created by Vadim on 16.07.22, 02:18
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 02:18
 *
 */

package com.example.vbook.domain.usecase.combination

import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.usecase.booktrakdownloader.GetLocalUriByDownloadIdUseCase
import com.example.vbook.domain.usecase.downloadingitem.GetMediaItemDownloadByOnlineUriUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAppropriateUriUseCase @Inject constructor(
    val getMediaItemDownloadByOnlineUriUseCase: GetMediaItemDownloadByOnlineUriUseCase,
    val getLocalUriByDownloadIdUseCase: GetLocalUriByDownloadIdUseCase
) {
    suspend operator fun invoke(uri: String): ResourceState<String> {
        getMediaItemDownloadByOnlineUriUseCase(uri).onSuccess { downloadingItem ->
            getLocalUriByDownloadIdUseCase(downloadingItem.downloadId).onSuccess {
                return ResourceState.Success(it)
            }
        }
        return ResourceState.Success(uri)
    }
}