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

class LoginViewModel : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _user = mutableStateOf(LoginRes())
    val user: State<LoginRes> = _user

    fun setUsername(userInput: String) {
        _loginState.value = _loginState.value.copy(username = userInput)
    }

    fun setPassword(userInput: String) {
        _loginState.value = _loginState.value.copy(password = userInput)
    }

    fun login(onLoginSuccess: () -> Unit, onLoginFail: () -> Unit) {
        viewModelScope.launch {
            try{
                _loginState.value = _loginState.value.copy(loading = true)

                _user.value = authService.login(
                    LoginReq(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                )
                onLoginSuccess()

                authInterceptor.updateToken(_user.value.accessToken)
            }
            catch (e: Exception) {
                Log.d("error login()", "$e")
                _loginState.value = _loginState.value.copy(error = e.toString())
                onLoginFail()
            }
            finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }

    fun createNewUser(onRegisterClick: () -> Unit) {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)

                authService.register(
                    LoginReq(
                        _loginState.value.username,
                        _loginState.value.password
                    )
                )

                onRegisterClick()
            }
            catch (e: Exception) {
                Log.d("error createNewUser()", "$e")
                _loginState.value = _loginState.value.copy(error = e.toString())
            }
            finally {
                _loginState.value = _loginState.value.copy(loading = false)
            }
        }
    }

    fun toggleShowPassword() {
        if (_loginState.value.showPassword) {
            _loginState.value = _loginState.value.copy(showPassword = false)
        }
        else _loginState.value = _loginState.value.copy(showPassword = true)
    }

    fun logout() {
        viewModelScope.launch {
            _user.value = _user.value.copy(accessToken = "", account = Account())
            authInterceptor.updateToken("")
        }
    }

}