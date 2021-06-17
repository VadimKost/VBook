package com.example.vbook.di

import com.example.vbook.data.InMemoryStorageImpl
import com.example.vbook.domain.common.InMemoryStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideStorage():InMemoryStorage=InMemoryStorageImpl()
}