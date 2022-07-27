package com.example.omdb.data.remote.api.deserializer

import com.example.omdb.data.model.entity.Genre
import com.example.omdb.data.model.MovieDetailWithGenres
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object MovieDetailWithGenresDeserializer : JsonDeserializer<MovieDetailWithGenres> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MovieDetailWithGenres = with(json.asJsonObject) {
        return MovieDetailWithGenres(
            detail = MovieDetailDeserializer.deserialize(json, typeOfT, context),
            genres = this["Genre"].asString.split(", ").map { title ->
                Genre(title = title)
            },
        )
    }

}