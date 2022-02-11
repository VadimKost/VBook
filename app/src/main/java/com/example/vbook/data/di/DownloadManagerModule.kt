/*
 * Created by Vadim on 11.02.2022, 12:19
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11.02.2022, 11:54
 *
 */

package com.example.vbook.data.di

import android.app.DownloadManager
import android.content.Context
import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.data.repository.MediaDownloadsRepositoryImpl
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.domain.repository.MediaDownloadsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DownloadManagerModule {

    @Provides
    @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}
