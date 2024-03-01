package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onLoginFail: () -> Unit) {
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
            modifier = Modifier.fillMaxSize().padding(horizontal = 58.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(text = "Username") },
                    value = loginVm.loginState.value.username,
                    onValueChange = {
                        loginVm.setUsername(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation =
                        if (loginVm.loginState.value.showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    placeholder = { Text(text = "Password") },
                    value = loginVm.loginState.value.password,
                    onValueChange = {
                        loginVm.setPassword(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password
                    ),
                    // Näppäimistöllä voidaan myös painaa nappia, jos fieldit on täytetty.
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (loginVm.loginState.value.username != ""
                                && loginVm.loginState.value.password != "")
                            {
                                loginVm.login(onLoginSuccess, onLoginFail)
                            }
                            else defaultKeyboardAction(imeAction = ImeAction.Previous)
                        }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { loginVm.toggleShowPassword() }) {
                            if (loginVm.loginState.value.showPassword) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "Show password"
                                )
                            }
                            else {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "Hide password"
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        loginVm.login(onLoginSuccess, onLoginFail)
                              },
                    enabled = loginVm.loginState.value.username != ""
                            && loginVm.loginState.value.password != ""
                ) {
                    Text(text = "Login")
                }

                Spacer(modifier = Modifier.height(36.dp))

                Row() {
                    Button(
                        onClick = {},
                        modifier = Modifier.size(120.dp,40.dp)
                    ) {
                        Text(text = "Register")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { onLoginSuccess() },
                        modifier = Modifier.size(120.dp,40.dp)
                    ) {
                        Text(text = "Skip")
                    }
                }



            }
        }
    }

}



