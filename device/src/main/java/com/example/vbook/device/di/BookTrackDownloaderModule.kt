/*
 * Created by Vadim on 16.07.22, 01:06
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.07.22, 01:06
 *
 */

package com.example.vbook.device.di

import android.app.DownloadManager
import android.content.Context
import com.example.vbook.device.booktrackdownloader.BookTrackDownloaderImpl
import com.example.vbook.domain.booktreackdownloader.BookTrackDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BookTrackDownloaderModule {
    @Provides
    @Singleton
    fun provideBookTrackDownloader(impl:BookTrackDownloaderImpl):BookTrackDownloader = impl

    @Provides
    @Singleton
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
}