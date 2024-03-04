package com.example.edistynytmobiiliohjelmointiprojekti.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.material3.TextButton
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
fun LoginScreen(onLoginSuccess: () -> Unit, onLoginFail: () -> Unit, onRegisterClick: () -> Unit) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 58.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    modifier = Modifier.requiredWidth(280.dp),
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
                    modifier = Modifier.requiredWidth(280.dp),
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
                        imeAction = (
                                if (loginVm.loginState.value.username == "" ) ImeAction.Previous
                                else if (loginVm.loginState.value.password == "" ) ImeAction.None
                                else ImeAction.Done
                                ),
                        keyboardType = KeyboardType.Password
                    ),
                    // Näppäimistöllä voidaan myös painaa nappia, jos fieldit on täytetty.
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (loginVm.loginState.value.password != "") {
                                defaultKeyboardAction(imeAction = ImeAction.Done)
                            }
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

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "No account?")
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onRegisterClick() }) {
                        Text(text = "Register here")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    TextButton(onClick = { onLoginSuccess() }) {
                        Text(text = "Continue as quest")
                    }
                }



/*
                Row() {
                    Button(
                        onClick = { onRegisterClick() },
                        modifier = Modifier.size(120.dp,40.dp)
                    ) {
                        Text(text = "Register")
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    Button(
                        onClick = { onLoginSuccess() },
                        modifier = Modifier.size(120.dp,40.dp)
                    ) {
                        Text(text = "Skip")
                    }
                }

*/

            }
        }
    }

}



