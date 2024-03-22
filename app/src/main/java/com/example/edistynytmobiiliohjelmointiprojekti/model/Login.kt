package com.example.edistynytmobiiliohjelmointiprojekti.model

import com.google.gson.annotations.SerializedName

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val showPassword: Boolean = false,
    val done: Boolean = false,
    val loggedIn: Boolean = false
)

data class AuthRole(
    @SerializedName("auth_role_id")
    val authRoleId: Int = 0,
    @SerializedName("role_name")
    val roleName: String = ""
)

data class Account(
    @SerializedName("auth_user_id")
    val authUserId: Int = 0,
    val username: String = "",
    @SerializedName("auth_role_auth_role")
    val authRole: AuthRole = AuthRole()
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

data class AccountRes(
    val username: String = "",
    val password: String = "",
    @SerializedName("auth_role_auth_role_id")
    val authRoleId: Int = 0,
    @SerializedName("access_jti")
    val accessJti: String = "",
    @SerializedName("auth_user_id")
    val authUserId: Int = 0,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("deleted_at")
    val deletedAt: String? = null
)
