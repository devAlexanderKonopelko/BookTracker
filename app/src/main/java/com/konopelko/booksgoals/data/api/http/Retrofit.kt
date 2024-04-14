package com.konopelko.booksgoals.data.api.http

import com.konopelko.booksgoals.data.api.BooksApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal fun createRetrofit(
	httpClient: OkHttpClient,
	hostUrl: String
): BooksApi =
	Retrofit
		.Builder()
		.baseUrl(hostUrl)
		.addConverterFactory(GsonConverterFactory.create())
		.client(httpClient)
		.build()
		.create(BooksApi::class.java)