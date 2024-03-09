package com.example.edistynytmobiiliohjelmointiprojekti.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response


val authInterceptor = AuthInterceptor("")

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .build()


class AuthInterceptor(private var token: String) : Interceptor {
    fun updateToken(newToken: String) {
        this.token = newToken
    }

    fun hasEmptyToken() : Boolean {
        return this.token == ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}
