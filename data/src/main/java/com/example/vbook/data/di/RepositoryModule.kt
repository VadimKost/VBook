/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.di

import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.domain.repository.DownloadingItemRepository
import com.example.vbook.data.repository.DownloadingItemRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(impl: BookRepositoryImpl): BookRepository = impl

    @Provides
    @Singleton
    fun provideMediaUriRepository(impl: DownloadingItemRepositoryImpl): DownloadingItemRepository = impl
}