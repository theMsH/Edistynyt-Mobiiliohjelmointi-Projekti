package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.AuthReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import retrofit2.http.Body
import retrofit2.http.POST

private val retrofit = createClient()

val authService = retrofit.create(AuthApi::class.java)

interface AuthApi {
    // POST::http://localhost:8000/api/v1/auth/login
    @POST("auth/login")
    suspend fun login(@Body authReq: AuthReq) : LoginRes
}