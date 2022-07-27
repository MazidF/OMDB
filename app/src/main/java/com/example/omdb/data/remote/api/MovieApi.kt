package com.example.omdb.data.remote.api

import com.example.omdb.data.model.MovieDetailWithGenres
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.entity.MovieDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("")
    suspend fun getMovieById(
        @Query("i") movieId: String,
    ): Response<List<Movie>>

    @GET("")
    suspend fun searchByTitle(
        @Query("s") title: String,
        @Query("page") page: Int,
    ): Response<List<Movie>>

    @GET("")
    suspend fun getDetail(
        // usually id is a part of path : @GET("/{id}") -->  @Path("id")
        @Query("i") movieId: String,
    ): Response<MovieDetailWithGenres>
}