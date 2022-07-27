package com.example.omdb.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = MovieGenreCrossRef.TABLE_NAME,
    primaryKeys = [
        "movie_id", "genre_title",
    ],
)
data class MovieGenreCrossRef(
    @ColumnInfo(name = "movie_id") val movieId: String,
    @ColumnInfo(name = "genre_title") val genreId: String,
){
    companion object {
        const val TABLE_NAME = "movie_genre_cross_ref_table"
    }
}