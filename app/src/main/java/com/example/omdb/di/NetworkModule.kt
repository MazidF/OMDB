package com.example.omdb.di

import com.example.omdb.data.IDataSource
import com.example.omdb.di.qualifier.AuthorizationInterceptor
import com.example.omdb.di.qualifier.LoggingInterceptor
import com.example.omdb.data.model.relation.MovieDetailWithGenres
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.data.remote.api.MovieApi
import com.example.omdb.data.remote.RemoteDataSource
import com.example.omdb.data.remote.api.deserializer.MovieDeserializer
import com.example.omdb.data.remote.api.deserializer.MovieDetailWithGenresDeserializer
import com.example.omdb.data.remote.api.deserializer.MovieResponseDeserializer
import com.example.omdb.di.qualifier.Remote
import com.example.omdb.utils.API_KEY
import com.example.omdb.utils.BASE_URL
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                // we want to get a list of movie but it returns an object (not array)
                // so we can create a class with list of movies for do something like this
            .registerTypeAdapter(object : TypeToken<ArrayList<Movie>>() {}.type, MovieResponseDeserializer)
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
                .addQueryParameter("apikey", API_KEY)
                .build()
            val newRequest = oldRequest.newBuilder()
                .url(newUrl)
                // .addHeader()   // add header if needed
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

    @Provides
    @Singleton
    @Remote
    fun provideRemoteDataSource(
        movieApi: MovieApi,
    ) : IDataSource {
        return RemoteDataSource(
            movieApi = movieApi,
        )
    }
}