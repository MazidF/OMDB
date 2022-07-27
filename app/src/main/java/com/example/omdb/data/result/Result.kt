package com.example.omdb.data.result

import com.example.omdb.data.result.error.Error
import com.example.omdb.data.result.error.SimpleError

sealed class Result<T>(
    private val data: T? = null,
    private val error: Error? = null,
) {

    class Loading<T> : Result<T>()

    class Fail<T>(error: Error) : Result<T>(error = error) {
        val error: Error get() = super.error!!
    }

    class Success<T>(
        data: T,
    ) : Result<T>(data = data) {
        val data: T get() = super.data!!
    }

    fun <R> map(transaction: (T) -> R): Result<R> {
        return when (this) {
            is Fail -> fail(error)
            is Loading -> loading()
            is Success -> success(transaction(data))
        }
    }

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success<T>(data)
        fun <T> fail(error: Error) = Fail<T>(error)
        fun <T> fail(error: Throwable) = fail<T>(SimpleError(error))
    }
}