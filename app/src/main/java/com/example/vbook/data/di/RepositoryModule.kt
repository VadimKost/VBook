package com.example.vbook.data.di

import com.example.vbook.data.repository.BookRepository.BookRepository
import com.example.vbook.data.repository.BookRepository.BookRepositoryImpl
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
//
//    @Provides
//    @Singleton
//    fun provideMediaUriRepository(impl: MediaDownloadsRepositoryImpl):MediaDownloadsRepository = impl
}