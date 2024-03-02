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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditCategoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryScreen(
    goBack: () -> Unit, 
    goToCategoriesScreen: () -> Unit
) {
    val vm : EditCategoryViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                title = { Text(text = vm.categoryTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            when{
                vm.categoryState.value.loading -> CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )

                vm.categoryState.value.error != null -> Text(text = "${vm.categoryState.value.error}")

                else -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RandomImage(600)

                    if (vm.availableNames.contains(vm.categoryState.value.categoryName)) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = "Category already exists!",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else Spacer(modifier = Modifier.height(30.dp))


                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier.requiredWidth(280.dp),
                        value = vm.categoryState.value.categoryName,
                        onValueChange = {
                            vm.setCategoryName(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Button(
                            onClick = {
                                goBack()
                            },
                            modifier = Modifier.size(120.dp,40.dp)
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        Button(
                            onClick = {
                                vm.updateCategoryById(goToCategoriesScreen)
                            },
                            modifier = Modifier.size(120.dp,40.dp),
                            enabled = (
                                    vm.categoryState.value.categoryName != "" &&
                                    !vm.availableNames.contains(vm.categoryState.value.categoryName)
                                    && vm.categoryState.value.categoryName != vm.categoryTitle
                                    )
                        ) {
                            Text(text = "Update")
                        }
                    }

                }
            }
        }
    }
}