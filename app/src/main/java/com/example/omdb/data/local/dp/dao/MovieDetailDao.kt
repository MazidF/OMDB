package com.example.omdb.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.omdb.data.local.dp.IDao
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.model.entity.MovieDetail

@Dao
interface MovieDetailDao : IDao<MovieDetail> {

    @Transaction
    @Query("select * from detail_table where detail_movie_id = :movieId")
    suspend fun getDetail(movieId: String): MovieDetailWithGenres?
}