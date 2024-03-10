package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.Account
import com.example.edistynytmobiiliohjelmointiprojekti.model.AccountRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


val authService: AuthApi = retrofit.create(AuthApi::class.java)

interface AuthApi {
    // POST::http://localhost:8000/api/v1/auth/login
    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq) : LoginRes

    @POST("auth/register")
    suspend fun register(@Body loginReq: LoginReq) : Account

    @Headers("Content-Type: application/json")
    @GET("auth/account")
    suspend fun getAccount() : AccountRes

}