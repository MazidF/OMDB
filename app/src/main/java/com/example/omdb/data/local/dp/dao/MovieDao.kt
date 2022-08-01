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

    @Query(
        "select * from movie_table where movie_title " +
                "like ('%' || :title || '%') limit :limit offset :offset"
    )
    suspend fun searchByTitle(
        title: String,
        limit: Int,
        offset: Int,
    ): List<Movie>

    @Query(
        "select movie_id, movie_title, movie_poster from movie_table " +
                "natural join movie_genre_cross_ref_table " +
                "where genre_title = :genre limit :limit offset :offset"
    )
    suspend fun getMoviesByGenre(
        genre: String,
        limit: Int,
        offset: Int,
    ): List<Movie>

    @Query("select * from movie_table limit :limit offset :offset")
    fun getMovies(
        limit: Int,
        offset: Int,
    ): Flow<List<Movie>>

    // temporary view genres
    @Query(
        "with genres as (select distinct mg.genre_title as title from movie_genre_cross_ref_table mg " +
                "where mg.movie_id = :movieId) " +
                "select distinct * from movie_table m " +
                "natural join movie_genre_cross_ref_table mg " +
                "where genre_title in genres limit :limit"
    )
    fun getSimilar(movieId: String, limit: Int): List<Movie>
}
