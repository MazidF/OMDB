package com.example.omdb.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.repository.MovieRepository
import com.example.omdb.data.result.Result
import com.example.omdb.utils.INITIAL_LOAD_SIZE
import com.example.omdb.utils.MAX_SIZE
import com.example.omdb.utils.PAGE_SIZE
import com.example.omdb.utils.PREFETCH_DISTANCE
import com.example.omdb.utils.helper.ConnectionHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class MovieUseCase(
    private val repository: MovieRepository,
    private val connectionHelper: ConnectionHelper,
    private val dispatcher: CoroutineDispatcher,
) {
    val connectionState = connectionHelper.connectionState

    private val pagingConfig by lazy {
        PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = true,
            initialLoadSize = INITIAL_LOAD_SIZE,
        )
    }

    fun resetConnectionState() {
        connectionHelper.resetConnectionState()
    }

    fun wifiSetting() {
        connectionHelper.goToWifiSettings()
    }

    fun cellularSetting() {
        connectionHelper.goToDataRoamingSettings()
    }

    private fun isNetworkAvailable() = connectionHelper.isNetworkAvailable()

    fun getDetail(movieId: String): Flow<Result<MovieDetailWithGenres>> {
        return repository.getDetail(isNetworkAvailable(), movieId)
    }

    fun getMovieById(movieId: String): Flow<Result<Movie>> {
        return repository.getMovieById(isNetworkAvailable(), movieId)
    }

    fun search(title: String): Flow<PagingData<Movie>> {
        return Pager(config = pagingConfig) {
            repository.search(isNetworkAvailable(), title)
        }.flow.flowOn(dispatcher)
    }
}
