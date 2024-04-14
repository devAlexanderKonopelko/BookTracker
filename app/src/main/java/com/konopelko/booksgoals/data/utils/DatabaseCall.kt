package com.konopelko.booksgoals.data.utils

import com.konopelko.booksgoals.data.utils.Result.Error
import com.konopelko.booksgoals.data.utils.Result.Success

suspend fun <T> databaseCall(apiCall: suspend () -> T): Result<T> =
    try {
        Success(apiCall.invoke())
    } catch (ex: Exception) {
        Error(exception = ex)
    }
