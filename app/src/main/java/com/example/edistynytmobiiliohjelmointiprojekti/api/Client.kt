package com.example.edistynytmobiiliohjelmointiprojekti.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val retrofit = createClient()

fun createClient(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/api/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
