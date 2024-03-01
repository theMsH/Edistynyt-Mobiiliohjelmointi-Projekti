package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import retrofit2.http.Body
import retrofit2.http.POST


val authService: AuthApi = retrofit.create(AuthApi::class.java)

interface AuthApi {
    // POST::http://localhost:8000/api/v1/auth/login
    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq) : LoginRes
}