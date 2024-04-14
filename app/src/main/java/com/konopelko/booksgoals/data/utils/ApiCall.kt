package com.konopelko.booksgoals.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T> apiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): Result<T> =
    withContext(dispatcher) {
        try {
            Result.Success(apiCall.invoke())
        } catch (ex: Exception) {
             Result.Error(exception = ex)
        }
    }

fun <T> Response<T>.catchErrors(): T =
    if (isSuccessful && body() != null) body()!!
    else throw Exception(errorBody()?.string() ?: "unknown error")