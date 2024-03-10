package com.example.edistynytmobiiliohjelmointiprojekti.view

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.R
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    onLoginSuccess: (accessToken: String?) -> Unit,
    onRegisterClick: () -> Unit
) {
    val vm : LoginViewModel = viewModel()
    val context = LocalContext.current


    LaunchedEffect(
        key1 = vm.loginState.value.done,
        key2 = vm.loginState.value.error
    ) {
        if (vm.loginState.value.done) {
            vm.setDone(false)
            onLoginSuccess(vm.user.value.accessToken)
        }

        when (vm.loginState.value.error) {
            "500" -> Toast.makeText(
                context,
                context.getString(R.string.connection_lost),
                Toast.LENGTH_LONG
            ).show()
            "404" -> Toast.makeText(
                context,
                context.getString(R.string.user_not_found),
                Toast.LENGTH_LONG
            ).show()
        }

        if (vm.loginState.value.error != null) {
            vm.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            // Loading
            vm.loginState.value.loading -> CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

            // If this has token, you are already logged in so go to categories.
            !authInterceptor.hasEmptyToken() -> onLoginSuccess(null)

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
                    placeholder = { Text(text = stringResource(R.string.username_placeholder)) },
                    value = vm.loginState.value.username,
                    onValueChange = {
                        vm.setUsername(it)
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
                        if (vm.loginState.value.showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    placeholder = { Text(text = stringResource(R.string.password_placeholder)) },
                    value = vm.loginState.value.password,
                    onValueChange = {
                        vm.setPassword(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = (
                                if (vm.loginState.value.username == "" ) ImeAction.Previous
                                else if (vm.loginState.value.password == "" ) ImeAction.None
                                else ImeAction.Done
                                ),
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (vm.loginState.value.password != "") {
                                defaultKeyboardAction(imeAction = ImeAction.Done)
                            }
                        }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { vm.toggleShowPassword() }) {
                            if (vm.loginState.value.showPassword) {
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
                        vm.login()
                              },
                    enabled = vm.loginState.value.username != ""
                            && vm.loginState.value.password != ""
                ) {
                    Text(text = stringResource(R.string.login))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.no_account))
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onRegisterClick() }) {
                        Text(text = stringResource(R.string.register_here))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    TextButton(onClick = { onLoginSuccess(null) }) {
                        Text(text = stringResource(R.string.continue_as_quest))
                    }
                }

            }
        }
    }

}



