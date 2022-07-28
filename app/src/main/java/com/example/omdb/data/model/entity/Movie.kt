package com.example.omdb.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = Movie.TABLE_NAME,
)
data class Movie(
    @PrimaryKey // pk is better to be int or long
    @ColumnInfo(name = "movie_id") val id: String,
    @ColumnInfo(name = "movie_title") val title: String,
    @ColumnInfo(name = "movie_poster") val poster: String,
) : Serializable {
    companion object {
        const val TABLE_NAME = "movie_table"
    }
}