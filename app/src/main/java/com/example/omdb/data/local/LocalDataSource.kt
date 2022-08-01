package com.example.omdb.data.local

import com.example.omdb.data.IDataSource
import com.example.omdb.data.local.dp.dao.GenreDao
import com.example.omdb.data.local.dp.dao.MovieDao
import com.example.omdb.data.local.dp.dao.MovieDetailDao
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result

class LocalDataSource(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao,
) : IDataSource {

    override suspend fun getMovieById(movieId: String): Result<Movie> {
        return Result.success(movieDao.getMovie(movieId)!!)
        // NullPointerException will be caught in flow
    }

    override suspend fun getDetail(movieId: String): Result<MovieDetailWithGenres> {
        return Result.success(movieDetailDao.getDetail(movieId)!!)
    }

    override suspend fun search(movieTitle: String, page: Int, pageSize: Int): Result<List<Movie>> {
        val list = movieDao.searchByTitle(
            movieTitle,
            offset = (page - 1) * pageSize,
            limit = pageSize,
        )
        return Result.success(list)
    }

    override suspend fun getSimilar(movieId: String): Result<List<Movie>> {
        return Result.success(movieDao.getSimilar(movieId, 10))
    }
}
