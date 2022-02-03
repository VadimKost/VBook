package com.example.vbook.domain.repository

import com.example.vbook.domain.common.Result

interface MediaUriRepository {
    fun getMediaUri(uri: String):Result<String>
}