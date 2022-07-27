package com.example.omdb.data.model

import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.entity.MovieDetail

data class MovieDetailWithGenres(
    val detail: MovieDetail,
    val genres: List<Genre>,
)