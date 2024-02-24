package com.example.edistynytmobiiliohjelmointiprojekti.model

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null
)

data class LoginRes(
    val id: Int = 1,
    val role: String = "normaluser",
)

data class AuthReq(
    val username: String = "",
    val password: String = ""
)