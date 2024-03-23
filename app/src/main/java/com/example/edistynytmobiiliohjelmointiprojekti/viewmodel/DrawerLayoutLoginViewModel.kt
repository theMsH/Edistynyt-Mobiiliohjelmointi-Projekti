package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.api.authService
import com.example.edistynytmobiiliohjelmointiprojekti.database.AccountDatabase
import com.example.edistynytmobiiliohjelmointiprojekti.database.DbProvider
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DrawerLayoutLoginViewModel(private val db: AccountDatabase = DbProvider.db): ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    init {
        tryAutoLogin()
    }

    fun setDone(done: Boolean) {
        _loginState.value = _loginState.value.copy(done = done)
    }

    fun clearError() {
        _loginState.value = _loginState.value.copy(error = null)
    }

    // Checks for existing token from RoomDB.
    // If found token is not valid for some reason it clears it.
    private fun tryAutoLogin() {
        viewModelScope.launch {
            val query = db.accessTokenDao().getAccessToken()

            if (query != null) {
                try {
                    authInterceptor.setToken(query.accessToken)

                    // Check if token is valid
                    authService.getAccount()

                    Log.d("CheckLoginState()", "Token found and valid, skip login")
                    login()
                }
                catch (e: Exception) {
                    if (e is HttpException) {
                        if (e.code() == 401) {
                            _loginState.value = _loginState.value.copy(error = "${e.code()}")
                        }
                        else _loginState.value = _loginState.value.copy(error = "$e")
                    } else _loginState.value = _loginState.value.copy(error = "$e")

                    // Autologin fail, remove token from RoomDB
                    authInterceptor.clearToken()
                    db.accessTokenDao().clearAccessToken()
                    _loginState.value = _loginState.value.copy(loggedIn = false)
                }
            }
            else Log.d("CheckLoginState()", "Login required")

            setDone(true)
            Log.d("CheckLoginState()", "LoginState check: Done")
        }
    }

    fun login() {
        _loginState.value = _loginState.value.copy(loggedIn = true)
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)

                authService.logout()

                // Clear tokens from authInterceptor and from RoomDB
                db.accessTokenDao().clearAccessToken()
                authInterceptor.clearToken()

                _loginState.value = _loginState.value.copy(loggedIn = false)
                Log.d("Logout()", "Successfully logout")
            }
            catch (e: Exception) {
                _loginState.value = _loginState.value.copy(error = e.toString())
                Log.d("Logout()", "$e")
            }
            finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }

}