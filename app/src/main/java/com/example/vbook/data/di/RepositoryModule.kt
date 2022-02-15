package com.example.vbook.data.di

import com.example.vbook.data.repository.book.BookRepository
import com.example.vbook.data.repository.book.BookRepositoryImpl
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepository
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepositoryImpl
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
    fun provideMediaUriRepository(impl: DownloadingItemRepositoryImpl):DownloadingItemRepository = impl
}