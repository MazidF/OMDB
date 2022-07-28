package com.example.omdb.data.remote.api

import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.model.entity.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.ArrayList

interface MovieApi {

    @GET("/") // use '/' as empty
    suspend fun getMovieById(
        @Query("i") movieId: String,
    ): Response<Movie>

    @GET("/")
    suspend fun searchByTitle(
        @Query("s") title: String,
        @Query("page") page: Int,
    ): Response<ArrayList<Movie>>

    @GET("/")
    suspend fun getDetail(
        // usually id is a part of path : @GET("/{id}") -->  @Path("id")
        @Query("i") movieId: String,
    ): Response<MovieDetailWithGenres>
}