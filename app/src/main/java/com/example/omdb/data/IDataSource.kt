package com.example.omdb.data

import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.result.Result

interface IDataSource {
    suspend fun getMovieById(movieId: String): Result<Movie>
    suspend fun getDetail(movieId: String): Result<MovieDetailWithGenres>
    suspend fun search(movieTitle: String, page: Int, pageSize: Int): Result<List<Movie>>
}
