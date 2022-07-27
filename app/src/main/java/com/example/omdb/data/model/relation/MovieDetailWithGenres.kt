package com.example.omdb.data.model.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.entity.MovieDetail
import com.example.omdb.data.model.entity.MovieGenreCrossRef

data class MovieDetailWithGenres(
    @Embedded val detail: MovieDetail,
    @Relation(
        parentColumn = "detail_movie_id",
        entityColumn = "genre_title",
        associateBy = Junction(
            value = MovieGenreCrossRef::class,
            parentColumn = "movie_id",
            entityColumn = "genre_title",
        ),
    ) val genres: List<Genre>,
)