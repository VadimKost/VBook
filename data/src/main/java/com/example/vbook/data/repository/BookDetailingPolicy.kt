/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.repository

import com.example.vbook.data.db.model.book.DetailedBook
import java.util.concurrent.TimeUnit

class BookDetailingUpdatePolicy {
    fun shouldUpdate(detailedBook: DetailedBook): Boolean {
        val createdAt = detailedBook.book.createdAt
        val modifiedAt = detailedBook.book.modifiedAt
        val deltaDays = TimeUnit.MILLISECONDS.toDays(modifiedAt - createdAt)

        if (createdAt == modifiedAt) return true
        return if (deltaDays >= 1) return true else false
    }
}