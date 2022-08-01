package com.example.omdb.data.local.dp

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.omdb.data.local.dp.dao.GenreDao
import com.example.omdb.data.local.dp.dao.MovieDao
import com.example.omdb.data.local.dp.dao.MovieDetailDao
import com.example.omdb.data.local.dp.dao.MovieGenreCrossRefDao
import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.entity.MovieDetail
import com.example.omdb.data.model.entity.MovieGenreCrossRef

@Database(
    entities = [
        Movie::class, MovieDetail::class, Genre::class, MovieGenreCrossRef::class,
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(
    value = [],
)
abstract class MovieDataBase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun movieGenreCrossRefDao(): MovieGenreCrossRefDao

    companion object {
        fun getMigrations(): Array<Migration> {
            return arrayOf(
                MIGRATION_1_2,
            )
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE detail_table ADD COLUMN detail_writers TEXT NOT NULL DEFAULT ('')"
                )
            }
        }
    }
}