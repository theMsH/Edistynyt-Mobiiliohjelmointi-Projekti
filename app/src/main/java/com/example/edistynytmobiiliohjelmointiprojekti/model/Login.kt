package com.example.edistynytmobiiliohjelmointiprojekti.model

import com.google.gson.annotations.SerializedName

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val showPassword: Boolean = false
)


data class LoginReq(
    val username: String = "",
    val password: String = ""
)

data class LoginRes(
    @SerializedName("access_token")
    val accessToken: String = "",
    val account: Account = Account()
)

data class Account(
    @SerializedName("auth_user_id")
    val authUserId: Int = 0,
    val userName: String = "",
    @SerializedName("auth_role_auth_role")
    val authRole: AuthRole = AuthRole()
)

data class AuthRole(
    @SerializedName("auth_role_id")
    val authRoleId: Int = 0,
    @SerializedName("role_name")
    val roleName: String = ""
)
