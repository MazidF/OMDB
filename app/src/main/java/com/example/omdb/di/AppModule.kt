package com.example.omdb.di

import com.example.omdb.data.IDataSaver
import com.example.omdb.data.IDataSource
import com.example.omdb.data.repository.MovieRepository
import com.example.omdb.di.qualifier.DispatcherIO
import com.example.omdb.di.qualifier.Local
import com.example.omdb.di.qualifier.Remote
import com.example.omdb.domain.MovieUseCase
import com.example.omdb.utils.helper.ConnectionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        @Local local: IDataSource,
        @Remote remote: IDataSource,
        saver: IDataSaver,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ): MovieRepository {
        return MovieRepository(
            local = local,
            remote = remote,
            saver = saver,
            dispatcher = dispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideMovieUseCase(
        repository: MovieRepository,
        connectionHelper: ConnectionHelper,
        @DispatcherIO dispatcher: CoroutineDispatcher,
    ): MovieUseCase {
        return MovieUseCase(
            repository = repository,
            connectionHelper = connectionHelper,
            dispatcher = dispatcher,
        )
    }
}
