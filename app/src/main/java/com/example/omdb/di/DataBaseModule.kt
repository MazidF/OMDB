package com.example.omdb.di

import android.content.Context
import androidx.room.Room
import com.example.omdb.data.IDataSaver
import com.example.omdb.data.IDataSource
import com.example.omdb.data.local.LocalDataSaver
import com.example.omdb.data.local.LocalDataSource
import com.example.omdb.data.local.dp.MovieDataBase
import com.example.omdb.data.local.dp.dao.GenreDao
import com.example.omdb.data.local.dp.dao.MovieDao
import com.example.omdb.data.local.dp.dao.MovieDetailDao
import com.example.omdb.di.qualifier.Local
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
        ).addMigrations(
            *MovieDataBase.getMigrations()
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(
        database: MovieDataBase,
    ) : MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieDetailDao(
        database: MovieDataBase,
    ) : MovieDetailDao {
        return database.movieDetailDao()
    }

    @Provides
    @Singleton
    fun provideGenreDao(
        database: MovieDataBase,
    ) : GenreDao {
        return database.genreDao()
    }

    @Provides
    @Singleton
    @Local
    fun provideLocalDataSource(
        genreDao: GenreDao,
        movieDao: MovieDao,
        movieDetailDao: MovieDetailDao,
    ) : IDataSource {
        return LocalDataSource(
            genreDao = genreDao,
            movieDao = movieDao,
            movieDetailDao = movieDetailDao,
        )
    }

    @Provides
    @Singleton
    fun provideLocalDataSaver(
        genreDao: GenreDao,
        movieDao: MovieDao,
        movieDetailDao: MovieDetailDao,
    ) : IDataSaver {
        return LocalDataSaver(
            genreDao = genreDao,
            movieDao = movieDao,
            movieDetailDao = movieDetailDao,
        )
    }
}
