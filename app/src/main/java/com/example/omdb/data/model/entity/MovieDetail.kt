package com.example.omdb.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = MovieDetail.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["movie_id"],
            childColumns = ["detail_movie_id"],
        ),
    ],
)
data class MovieDetail(
    @PrimaryKey
    @ColumnInfo(name = "detail_movie_id") val movieId: String,
    @ColumnInfo(name = "detail_description") val description: String,
    @ColumnInfo(name = "detail_duration") val duration: String,
    @ColumnInfo(name = "detail_imdb_rate") val imdbRate: Float,
    @ColumnInfo(name = "detail_writers") val writers: String,
    @ColumnInfo(name = "detail_actors") val actors: String,
    @ColumnInfo(name = "detail_country") val country: String,
    @ColumnInfo(name = "detail_language") val language: String,
    @ColumnInfo(name = "detail_release_time") val releaseTime: String,
) {
    companion object {
        const val TABLE_NAME = "detail_table"
    }
}