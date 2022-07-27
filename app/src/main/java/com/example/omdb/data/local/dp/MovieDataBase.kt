package com.example.omdb.data.local.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.entity.MovieDetail
import com.example.omdb.data.model.entity.MovieGenreCrossRef

@Database(
    entities = [
        Movie::class, MovieDetail::class, Genre::class, MovieGenreCrossRef::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [],
)
abstract class MovieDataBase : RoomDatabase()