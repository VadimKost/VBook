/*
 * Created by Vadim on 21.07.22, 23:06
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 13.07.22, 12:19
 *
 */

package com.example.vbook.data.db.mapper

import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.model.Book

fun Book.mapToData() = BookEntity(
    source,
    bookUrl,
    title,
    author,
    reader,
    coverURL,
    mediaItems,
    cycle,
    cycleBookList,
    duration,
    isFavorite = isFavorite,
    stoppedTrackIndex,
    stoppedTrackTime
)

fun List<Book>.mapToData() = this.map { it.mapToData() }

fun BookEntity.mapToDomain() = Book(
    source,
    bookURL,
    title,
    author,
    reader,
    coverURL,
    mediaItems,
    cycle,
    cycleBookList,
    duration,
    isFavorite = isFavorite,
    stoppedTrackIndex,
    stoppedTrackTime
)

fun List<BookEntity>.mapToDomain() = this.map { it.mapToDomain() }

