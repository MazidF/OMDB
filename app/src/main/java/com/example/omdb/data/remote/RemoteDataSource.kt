package com.example.omdb.data.remote

import com.example.omdb.data.IDataSource
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.remote.api.MovieApi
import com.example.omdb.data.result.Result
import com.example.omdb.data.result.error.NetworkError
import com.example.omdb.utils.asResult
import retrofit2.Response

class RemoteDataSource(
    private val movieApi: MovieApi,
) : IDataSource {

    override suspend fun getMovieById(movieId: String): Result<Movie> {
        return movieApi.getMovieById(movieId).asResult()
    }

    override suspend fun getDetail(movieId: String): Result<MovieDetailWithGenres> {
        return movieApi.getDetail(movieId).asResult()
    }

    override suspend fun search(movieTitle: String, page: Int, pageSize: Int): Result<List<Movie>> {
        return movieApi.searchByTitle(movieTitle, page).asResult().map {
            it.toList()
        }
    }

    override suspend fun getSimilar(movieId: String): Result<List<Movie>> {
        return Result.fail(NetworkError.NullBodyError(Response.success(null)))
    }
}