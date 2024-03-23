package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.Account
import com.example.edistynytmobiiliohjelmointiprojekti.model.AccountRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


val authService: AuthApi = retrofit.create(AuthApi::class.java)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body loginReq: LoginReq) : LoginRes

    @POST("auth/logout")
    suspend fun logout()

    @POST("auth/register")
    suspend fun register(@Body loginReq: LoginReq) : Account

    @GET("auth/account")
    suspend fun getAccount() : AccountRes

}