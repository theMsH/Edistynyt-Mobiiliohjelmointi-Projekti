package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.api.authService
import com.example.edistynytmobiiliohjelmointiprojekti.model.Account
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _user = mutableStateOf(LoginRes())


    fun setUsername(username: String) {
        _loginState.value = _loginState.value.copy(username = username)
    }

    fun setPassword(password: String) {
        _loginState.value = _loginState.value.copy(password = password)
    }

    fun setDone(done: Boolean) {
        _loginState.value = _loginState.value.copy(done = done)
    }

    fun clearError() {
        _loginState.value = _loginState.value.copy(error = null)
    }

    fun toggleShowPassword() {
        if (_loginState.value.showPassword) {
            _loginState.value = _loginState.value.copy(showPassword = false)
        }
        else _loginState.value = _loginState.value.copy(showPassword = true)
    }

    fun login() {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)

                _user.value = authService.login(
                    LoginReq(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                )
                authInterceptor.updateToken(_user.value.accessToken)
                _loginState.value = _loginState.value.copy(done = true)
            }
            catch (e: Exception) {
                Log.d("error login()", "$e")
                if (e is HttpException) {
                    _loginState.value = _loginState.value.copy(error = e.code().toString())
                }
                else _loginState.value = _loginState.value.copy(error = e.toString())

            }
            finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _user.value = _user.value.copy(accessToken = "", account = Account())
            authInterceptor.updateToken("")
        }
    }

    fun createNewUser() {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)

                authService.register(
                    LoginReq(
                        _loginState.value.username,
                        _loginState.value.password
                    )
                )
                _loginState.value = _loginState.value.copy(done = true)
            }
            catch (e: Exception) {
                Log.d("error createNewUser()", "$e")
                if (e is HttpException) {
                    _loginState.value = _loginState.value.copy(error = e.code().toString())
                }
                else _loginState.value = _loginState.value.copy(error = e.toString())
            }
            finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }

}