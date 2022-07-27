package com.example.omdb.data

import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.relation.MovieDetailWithGenres

interface IDataSaver {
    suspend fun saveMovieById(movie: Movie)
    suspend fun saveDetail(detail: MovieDetailWithGenres)
    suspend fun saveMovies(movies: List<Movie>)
}