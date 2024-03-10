package com.example.edistynytmobiiliohjelmointiprojekti.view

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
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditRentalItemViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRentalItemScreen(
    goBack: () -> Unit,
    goToRentalItemsScreen: (CategoryItem) -> Unit
) {
    val vm: EditRentalItemViewModel = viewModel()
    val configuration = LocalConfiguration.current

    LaunchedEffect(key1 = vm.rentalItemState.value.done) {
        if (vm.rentalItemState.value.done) {
            vm.setDone(false)
            goToRentalItemsScreen(vm.categoryItem)
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
                title = { Text(text = vm.rentalItemState.value.rentalItemTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                // Loading
                vm.rentalItemState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                // Error
                vm.rentalItemState.value.error != null ->
                    Text(text = "${vm.rentalItemState.value.error}")


                // Orientation: Portrait
                configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RandomImage(600, vm.rentalItemState.value.rentalItem.rentalItemId)

                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier.requiredWidth(280.dp),
                        value = vm.rentalItemState.value.rentalItem.rentalItemName,
                        onValueChange = {newValue ->
                            vm.setRentalItemName(newValue)
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
                            onClick = { vm.updateRentalItemName() },
                            modifier = Modifier.size(120.dp,40.dp),
                            enabled = (
                                    vm.rentalItemState.value.rentalItem.rentalItemName != ""
                                            && vm.rentalItemState.value.rentalItem.rentalItemName != vm.rentalItemState.value.rentalItemTitle
                                    )
                        ) {
                            Text(text = stringResource(R.string.update))
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }


                // Orientation: Horizontal
                else -> Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Error used to print here. Now used as a height spacer
                    Text(text = "",modifier = Modifier.padding(vertical = 10.dp))

                    OutlinedTextField(
                        singleLine = true,
                        modifier = Modifier.requiredWidth(280.dp),
                        value = vm.rentalItemState.value.rentalItem.rentalItemName,
                        onValueChange = {newValue ->
                            vm.setRentalItemName(newValue)
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
                            onClick = { vm.updateRentalItemName() },
                            modifier = Modifier.size(120.dp,40.dp),
                            enabled = (
                                    vm.rentalItemState.value.rentalItem.rentalItemName != ""
                                    && vm.rentalItemState.value.rentalItem.rentalItemName != vm.rentalItemState.value.rentalItemTitle
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

