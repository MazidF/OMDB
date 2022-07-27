package com.example.omdb.data.result.error

class SimpleError(
    private val error: Throwable,
) : Error(error.message.toString())