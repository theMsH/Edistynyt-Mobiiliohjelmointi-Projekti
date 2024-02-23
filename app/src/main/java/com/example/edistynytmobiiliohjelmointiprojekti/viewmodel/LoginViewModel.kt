package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun setUsername(userInput: String) {
        _loginState.value = _loginState.value.copy(username = userInput)
    }

    fun setPassword(userInput: String) {
        _loginState.value = _loginState.value.copy(password = userInput)
    }

    fun login() {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(loading = true)
            fakeLoading()
            val res = LoginRes()
            _loginState.value = _loginState.value.copy(loading = false)
        }
    }

    private suspend fun fakeLoading() {
        delay(1000)
    }
}