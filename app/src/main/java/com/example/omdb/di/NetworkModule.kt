package com.example.omdb.di

import com.example.learning.di.qualifier.AuthorizationInterceptor
import com.example.learning.di.qualifier.LoggingInterceptor
import com.example.omdb.data.model.MovieDetailWithGenres
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.model.entity.MovieDetail
import com.example.omdb.data.remote.api.MovieApi
import com.example.omdb.data.remote.api.deserializer.MovieDeserializer
import com.example.omdb.data.remote.api.deserializer.MovieDetailDeserializer
import com.example.omdb.data.remote.api.deserializer.MovieDetailWithGenresDeserializer
import com.example.omdb.utils.API_KEY
import com.example.omdb.utils.BASE_URL
import com.google.gson.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Movie::class.java, MovieDeserializer)
            .registerTypeAdapter(MovieDetailWithGenres::class.java, MovieDetailWithGenresDeserializer)
            .create()
    }

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @AuthorizationInterceptor
    fun provide(): Interceptor {
        return Interceptor { chain ->
            val oldRequest = chain.request()
            val oldUrl = oldRequest.url()

            val newUrl = oldUrl.newBuilder()
                // .addQueryParameter()   // add query parameter if needed
                .build()
            val newRequest = oldRequest.newBuilder()
                .url(newUrl)
                .addHeader("apikey", API_KEY)
                .build()

            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @LoggingInterceptor logger: Interceptor,
        @AuthorizationInterceptor authorizer: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(authorizer)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(
        retrofit: Retrofit,
    ) : MovieApi {
        return retrofit.create(MovieApi::class.java)
    }
}