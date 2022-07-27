package com.example.omdb.data.remote.api.deserializer

import com.example.omdb.data.model.entity.Movie
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object MovieDeserializer : JsonDeserializer<Movie> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Movie = with(json.asJsonObject) {
        return Movie(
            id = this["imdbID"].asString,
            title = this["Title"].asString,
            poster = this["Poster"].asString,
        )
    }

}