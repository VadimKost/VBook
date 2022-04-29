package com.example.vbook.data.db.mapper

import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.common.model.Book

fun Book.mapToData() = BookEntity(
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

