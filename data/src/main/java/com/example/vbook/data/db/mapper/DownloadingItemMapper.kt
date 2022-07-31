/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

import com.example.vbook.domain.model.DownloadingItem
import com.example.vbook.data.db.model.DownloadingItemEntity

fun DownloadingItem.toData() = DownloadingItemEntity(
    downloadId, onlineUri, bookUrl
)

fun List<DownloadingItem>.toData() = this.map { it.toData() }

fun DownloadingItemEntity.toDomain() = DownloadingItem(
    downloadId, onlineUri, bookUrl
)

fun List<DownloadingItemEntity>.toDomain() = this.map { it.toDomain() }