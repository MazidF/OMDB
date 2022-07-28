package com.example.omdb.data.remote.api.deserializer

import com.example.omdb.data.model.entity.Movie
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

object MovieResponseDeserializer : JsonDeserializer<List<Movie>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Movie> {
        with(json.asJsonObject) {
            val array = this["Search"].asJsonArray
            return List(array.size()) {
                MovieDeserializer.deserialize(array[it], typeOfT, context)
            }
        }
    }

}