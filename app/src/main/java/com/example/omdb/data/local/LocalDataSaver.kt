package com.example.omdb.data.local

import androidx.room.Transaction
import com.example.omdb.data.IDataSaver
import com.example.omdb.data.local.dp.dao.GenreDao
import com.example.omdb.data.local.dp.dao.MovieDao
import com.example.omdb.data.local.dp.dao.MovieDetailDao
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres

class LocalDataSaver(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao,
) : IDataSaver {

    override suspend fun saveMovieById(movie: Movie) {
        movieDao.insert(movie)
    }

    @Transaction
    override suspend fun saveDetail(detail: MovieDetailWithGenres) {
        movieDetailDao.insert(detail.detail)
        genreDao.insert(*detail.genres.toTypedArray())

    }

    override suspend fun saveMovies(movies: List<Movie>) {
        movieDao.insert(*movies.toTypedArray())
    }
}