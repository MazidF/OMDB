package com.example.omdb.utils

import com.example.omdb.data.result.Result
import com.example.omdb.data.result.error.NetworkError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response


fun <T> Response<T>.isSuccessFul(): Boolean {
    return isSuccessful and (body() != null) and (code() in 200 until 300)
}

fun <T> Response<T>.asResult(): Result<T> {
    return if (isSuccessFul()) {
        Result.success(body()!!)
    } else {
        val error = NetworkError.get(this)
        Result.fail(error)
    }
}

fun <T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Result<T>,
): Flow<Result<T>> {
    return flow {
        emit(block())
    }.onStart {
        emit(Result.loading())
    }.catch { cause ->
        emit(Result.fail(cause))
    }.flowOn(dispatcher)
}