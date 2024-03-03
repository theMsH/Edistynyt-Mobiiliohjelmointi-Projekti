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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    goBack: () -> Unit
) {
    val vm: LoginViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                                 },
                title = { Text(text = "Create account") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Loading
                vm.loginState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                vm.loginState.value.error != null -> Text(text = "${vm.loginState.value.error}")


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
                        visualTransformation = (
                                if (vm.loginState.value.showPassword) VisualTransformation.None
                                else PasswordVisualTransformation()
                                ),
                        placeholder = { Text(text = "Password") },
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
                        // Näppäimistöllä voidaan myös painaa nappia, jos fieldit on täytetty.
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
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.VisibilityOff,
                                        contentDescription = "Hide password"
                                    )
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Button(
                            onClick = { goBack() },
                            modifier = Modifier.size(120.dp, 40.dp)
                        ) {
                            Text(text = "Cancel")
                        }

                        Spacer(modifier = Modifier.width(32.dp))

                        Button(
                            onClick = { vm.createNewUser(onRegisterClick) },
                            modifier = Modifier.size(120.dp, 40.dp),
                            enabled = (
                                    vm.loginState.value.username != ""
                                    && vm.loginState.value.password != ""
                                    )
                        ) {
                            Text(text = "Register")
                        }
                    }

                }
            }
        }
    }

}