package com.konopelko.booksgoals.di.network

import com.konopelko.booksgoals.BuildConfig
import com.konopelko.booksgoals.data.api.http.createHttpClient
import com.konopelko.booksgoals.data.api.http.createRetrofit
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val HTTP_CLIENT_BASE = "api.client.base"

val networkModule = module {

    single(named(HTTP_CLIENT_BASE)) {
        createHttpClient()
    }

    single {
        createRetrofit(
            httpClient = get(named(HTTP_CLIENT_BASE)),
            hostUrl = BuildConfig.BOOKS_API_HOST
        )
    }
}