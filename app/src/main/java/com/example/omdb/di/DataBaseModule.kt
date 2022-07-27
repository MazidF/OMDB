package com.example.omdb.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.omdb.data.local.dp.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideMovieDataBase(
        @ApplicationContext context: Context,
    ) : MovieDataBase {
        return Room.databaseBuilder(
            context,
            MovieDataBase::class.java,
            MovieDataBase::class.java.simpleName,
        ).build()
    }
}
