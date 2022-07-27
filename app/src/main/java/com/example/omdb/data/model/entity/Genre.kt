package com.example.omdb.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = Genre.TABLE_NAME,
)
data class Genre(
    @PrimaryKey
    @ColumnInfo(name = "genre_title") val title: String,
) {
    companion object {
        const val TABLE_NAME = "genre_table"
    }
}