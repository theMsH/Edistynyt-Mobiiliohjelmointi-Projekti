package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    val loginVm : LoginViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            // Loading
            loginVm.loginState.value.loading -> CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

            // Ready
        else -> Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    singleLine = true,
                    placeholder = { Text(text = "Username") },
                    value = loginVm.loginState.value.username,
                    onValueChange = {
                        loginVm.setUsername(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    singleLine = true,
                    placeholder = { Text(text = "Password") },
                    value = loginVm.loginState.value.password,
                    onValueChange = {
                        loginVm.setPassword(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    // Näppäimistöllä voidaan myös painaa nappia, jos fieldit on täytetty.
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (loginVm.loginState.value.username != ""
                                && loginVm.loginState.value.password != "")
                            {
                                onLoginClick()
                                loginVm.login()
                            }
                            else defaultKeyboardAction(imeAction = ImeAction.Previous)
                        }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onLoginClick()
                        loginVm.login()
                              },
                    enabled = loginVm.loginState.value.username != ""
                            && loginVm.loginState.value.password != ""
                ) {
                    Text(text = "Login")
                }

            }
        }
    }

}




