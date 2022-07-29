package com.example.omdb.data.remote.api.deserializer

import com.example.omdb.data.model.entity.MovieDetail
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object MovieDetailDeserializer : JsonDeserializer<MovieDetail> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MovieDetail = with(json.asJsonObject) {
        return MovieDetail(
            movieId = this["imdbID"].asString,
            description = this["Plot"].asString,
            duration = this["Runtime"].asString,
            imdbRate = this["imdbRating"].asFloat,
            writers = this["Writer"].asString,
            actors = this["Actors"].asString,
            country = this["Country"].asString,
            language = this["Language"].asString,
            releaseTime = this["Released"].asString,
        )
    }

}