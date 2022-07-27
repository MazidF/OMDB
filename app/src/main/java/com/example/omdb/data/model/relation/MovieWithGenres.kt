package com.example.omdb.data.model.relation

import androidx.room.*
import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.entity.MovieGenreCrossRef

data class MovieWithGenres(
    @Embedded val movie: Movie,
    @Relation(
        entity = Genre::class,
        parentColumn = "movie_id",
        entityColumn = "genre_title",
        associateBy = Junction(MovieGenreCrossRef::class)
    ) val genres: List<Genre>,
)
