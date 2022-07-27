package com.example.omdb.data.result.error

import android.util.Log
import retrofit2.Response

sealed class NetworkError(
    response: Response<*>,
    message: String? = null,
) : Error(message ?: response.message()) {

    val statusCode = response.code()

    class NullBodyError(response: Response<*>) : NetworkError(
        response, message = "Response body is null"
    )
    class ServerError(response: Response<*>) : NetworkError(
        response, message = "Something is wrong with server"
    )

    class Unknown(response: Response<*>) : NetworkError(response)
    class ClientError(response: Response<*>) : NetworkError(response)

    companion object {
        fun <T> get(response: Response<T>): NetworkError {
            if (response.body() == null) {
                return NullBodyError(response)
            }

            val status = response.code()
            return when {
                status >= 500 -> ServerError(response)
                status >= 400 -> ClientError(response)
                status in 200 until 300 -> {
                    throw IllegalStateException("Status code($status) is not an error")
                }
                else -> {
                    Log.e("new_status", status.toString())
                    Unknown(response)
                }
            }
        }
    }
}