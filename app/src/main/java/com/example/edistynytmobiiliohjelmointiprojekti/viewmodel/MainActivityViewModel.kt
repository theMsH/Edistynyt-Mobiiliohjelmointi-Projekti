package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.database.DbProvider
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginState
import kotlinx.coroutines.launch

class MainActivityViewModel(): ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    init {
        checkLoginState()
    }

    fun setDone(done: Boolean) {
        _loginState.value = _loginState.value.copy(done = done)
    }

    private fun checkLoginState() {
        viewModelScope.launch {
            val query = DbProvider.db.accessTokenDao().getAccessToken()

            if (query != null) {
                Log.d("CheckLoginState()", "Token found, skip login")
                authInterceptor.setToken(query.accessToken)
                _loginState.value = _loginState.value.copy(loggedIn = true)
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
        _loginState.value = _loginState.value.copy(loggedIn = false)

        authInterceptor.setToken("")

        viewModelScope.launch {
            DbProvider.db.accessTokenDao().clearAccessToken()
        }
    }

}