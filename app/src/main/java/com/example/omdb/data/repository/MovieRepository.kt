package com.example.omdb.data.repository

import androidx.lifecycle.LiveData
import com.example.omdb.data.IDataSaver
import com.example.omdb.data.IDataSource
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.paging.ItemPagingSource
import com.example.omdb.data.result.Result
import com.example.omdb.utils.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val local: IDataSource,
    private val remote: IDataSource,
    private val saver: IDataSaver,
    private val dispatcher: CoroutineDispatcher,  // for testing
) {
    private val scope by lazy {
        CoroutineScope(dispatcher + SupervisorJob())
    }

    suspend fun search(isOnline: Boolean, title: String, page: Int, perPage: Int): Result<List<Movie>> {
        val result = if (isOnline) {
            remote.search(title, page, perPage)
        } else {
            local.search(title, page, perPage)
        }

        if (isOnline and (result is Result.Success)) {
            saver.saveMovies((result as Result.Success).data())
        }

        return result
    }

    fun getDetail(isOnline: Boolean, movieId: String): Flow<Result<MovieDetailWithGenres>> {
        return loadAndStore(
            isOnline = isOnline,
            remoteLoad = {
                remote.getDetail(movieId)
            },
            localLoad = { local.getDetail(movieId) },
            saver = { saver.saveDetail(it) },
        )
    }

    fun getMovieById(isOnline: Boolean, movieId: String): Flow<Result<Movie>> {
        return loadAndStore(
            isOnline = isOnline,
            remoteLoad = {
                remote.getMovieById(movieId)
            },
            localLoad = { local.getMovieById(movieId) },
            saver = { saver.saveMovieById(it) },
        )
    }

    private suspend fun <T> loadData(
        isOnline: Boolean,
        remoteLoad: suspend () -> T,
        localLoad: suspend () -> T,
    ): T {
        return if (isOnline) {
            remoteLoad()
        } else {
            localLoad()
        }
    }

    private suspend fun <T> saveData(
        isOnline: Boolean,
        result: Result<T>,
        saver: suspend (T) -> Unit,
    ) {
        if (isOnline and (result is Result.Success)) {
            val data = (result as Result.Success).data()
            saver(data)
        }
    }

    private fun <T : Any> loadAndStore(
        isOnline: Boolean,
        remoteLoad: suspend () -> Result<T>,
        localLoad: suspend () -> Result<T>,
        saver: suspend (T) -> Unit,
    ): Flow<Result<T>> {
        return safeApiCall(dispatcher) {
            val result = loadData(isOnline, remoteLoad, localLoad)
            saveData(isOnline, result, saver)
            result
        }
    }
}