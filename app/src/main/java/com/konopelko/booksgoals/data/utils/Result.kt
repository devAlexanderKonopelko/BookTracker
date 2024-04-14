package com.konopelko.booksgoals.data.utils

sealed class Result<out T> {
    data class Success<out T>(val value: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()

    suspend fun onSuccess(function: suspend (data: T) -> Unit): Result<T> {
        if(this is Success)
            function.invoke(this.value)
        return this
    }

    suspend fun <G> map(function: suspend (result: Success<T>) -> Success<G>): Success<G> {
        return function.invoke(this as Success<T>)
    }

    suspend fun onError(function: suspend (result: Error) -> Unit): Result<T> {
        if(this is Error)
            function.invoke(this)
        return this
    }
}







