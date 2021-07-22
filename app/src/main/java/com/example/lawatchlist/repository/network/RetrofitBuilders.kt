package com.example.lawatchlist.repository.network

import android.util.Log
import com.example.lawatchlist.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//private const val BASE_URL = "http://api.tvmaze.com"
private const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = BuildConfig.tmbdkey

val logger = HttpLoggingInterceptor { Log.d("API", it) }.apply {
   level = HttpLoggingInterceptor.Level.BASIC
}

fun buildClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()

fun buildRetrofit(): Retrofit = Retrofit.Builder()
    .client(buildClient())
    .addConverterFactory(MoshiConverterFactory.create().asLenient())
    .baseUrl(BASE_URL)
    .build()

fun builApiService () :MovieApiService = buildRetrofit()
    .create(MovieApiService::class.java)

