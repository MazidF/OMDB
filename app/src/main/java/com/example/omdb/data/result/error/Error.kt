package com.example.omdb.data.result.error

sealed class Error(
    private val message: String,
) {

    fun toThrowable() = Throwable(message)
}