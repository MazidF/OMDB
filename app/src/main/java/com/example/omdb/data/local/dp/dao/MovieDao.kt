package com.example.omdb.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.omdb.data.local.dp.IDao
import com.example.omdb.data.model.entity.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : IDao<Movie> {

    @Query("select * from movie_table where movie_id = :movieId")
    suspend fun getMovie(movieId: String): Movie?

    @Query("select * from movie_table where movie_title like '%' || :title || '%' ")
    suspend fun searchByTitle(title: String)

    @Query(
        "select * from movie_table natural join movie_genre_cross_ref_table " +
                "where genre_title = :genre limit :limit offset :offset"
    )
    suspend fun getMoviesByGenre(
        genre: String,
        limit: Int,
        offset: Int,
    ): Flow<List<Movie>>

    @Query("select * from movie_table limit :limit offset :offset")
    fun getMovies(
        limit: Int,
        offset: Int,
    ): Flow<List<Movie>>
}
