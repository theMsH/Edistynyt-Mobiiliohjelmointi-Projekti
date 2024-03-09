package com.example.edistynytmobiiliohjelmointiprojekti.screen

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.R
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditCategoryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryScreen(
    goBack: () -> Unit, 
    goToCategoriesScreen: () -> Unit
) {
    val vm : EditCategoryViewModel = viewModel()
    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = vm.categoryState.value.done) {
        if (vm.categoryState.value.done) {
            vm.setDone(false)
            goToCategoriesScreen()
        }
    }

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
                title = { Text(text = vm.categoryState.value.categoryTitle) },
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
                vm.categoryState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                // Error
                vm.categoryState.value.error != null -> Text(text = "${vm.categoryState.value.error}")


                // Orientation: Portrait
                configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RandomImage(600, vm.categoryState.value.categoryItem.categoryId)

                    if (vm.categoryState.value.nonValidNamesList.contains(
                            vm.categoryState.value.categoryItem.categoryName)
                        &&
                        vm.categoryState.value.categoryItem.categoryName !=
                            vm.categoryState.value.categoryTitle) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = stringResource(R.string.category_exists_alert),
                            color = MaterialTheme.colorScheme.error
                        )
                    } else Text(text = "", modifier = Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier.requiredWidth(280.dp),
                        value = vm.categoryState.value.categoryItem.categoryName,
                        onValueChange = {
                            vm.setCategoryName(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Button(
                            onClick = { goBack() },
                            modifier = Modifier.size(120.dp, 40.dp)
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(32.dp))

                        Button(
                            onClick = { vm.updateCategoryById() },
                            modifier = Modifier.size(120.dp, 40.dp),
                            enabled = (
                                    vm.categoryState.value.categoryItem.categoryName != ""
                                    &&
                                    !vm.categoryState.value.nonValidNamesList.contains(
                                        vm.categoryState.value.categoryItem.categoryName)
                                    &&
                                    vm.categoryState.value.categoryItem.categoryName !=
                                            vm.categoryState.value.categoryTitle
                                    )
                        ) {
                            Text(text = stringResource(R.string.update))
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }


                // Orientation: Horizontal
                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (vm.categoryState.value.nonValidNamesList.contains(
                            vm.categoryState.value.categoryItem.categoryName)
                        &&
                        vm.categoryState.value.categoryItem.categoryName !=
                            vm.categoryState.value.categoryTitle) {
                        Text(
                            modifier = Modifier.padding(vertical = 10.dp),
                            text = stringResource(R.string.category_exists_alert),
                            color = MaterialTheme.colorScheme.error
                        )
                    } else Text(text = "",modifier = Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier.requiredWidth(280.dp),
                        value = vm.categoryState.value.categoryItem.categoryName,
                        onValueChange = {
                            vm.setCategoryName(it)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row {
                        Button(
                            onClick = { goBack() },
                            modifier = Modifier.size(120.dp, 40.dp)
                        ) {
                            Text(text =  stringResource(R.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(32.dp))

                        Button(
                            onClick = { vm.updateCategoryById() },
                            modifier = Modifier.size(120.dp,40.dp),
                            enabled = (
                                    vm.categoryState.value.categoryItem.categoryName != ""
                                    &&
                                    !vm.categoryState.value.nonValidNamesList.contains(
                                        vm.categoryState.value.categoryItem.categoryName)
                                    &&
                                    vm.categoryState.value.categoryItem.categoryName !=
                                            vm.categoryState.value.categoryTitle
                                    )
                        ) {
                            Text(text = stringResource(R.string.update))
                        }
                    }

                }
            }
        }
    }

}