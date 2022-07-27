package com.example.omdb.di

import com.example.onlineshop.di.qualifier.DispatcherIO
import com.example.onlineshop.di.qualifier.DispatcherMain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Provides
    @Singleton
    @DispatcherIO
    fun provideDispatcherIO() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    @DispatcherMain
    fun provideDispatcherMain() : CoroutineDispatcher {
        return Dispatchers.Main
    }

}