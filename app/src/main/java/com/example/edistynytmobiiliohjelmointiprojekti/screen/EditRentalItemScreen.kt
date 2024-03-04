package com.example.edistynytmobiiliohjelmointiprojekti.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditRentalItemViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRentalItemScreen(goBack: () -> Unit, goToRentalItemsScreen: (Int, String) -> Unit) {
    val vm: EditRentalItemViewModel = viewModel()

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
                title = { Text(text = vm.rentalItemTitle.value) },
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
                vm.rentalItemState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                vm.rentalItemState.value.error != null ->
                    Text(text = "error: ${vm.rentalItemState.value.error}")

                else -> Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
                    Button(
                        onClick = {
                            vm.updateRentalItemName(goToRentalItemsScreen)
                                  },
                        modifier = Modifier.size(120.dp,40.dp),
                        enabled = (
                                vm.rentalItemState.value.rentalItem.rentalItemName != ""
                                && vm.rentalItemState.value.rentalItem.rentalItemName != vm.rentalItemTitle.value
                                )
                    ) {
                        Text(text = "Update")
                    }



                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Status: ${
                        vm.rentalItemState.value.rentalItem.rentalState.rentalState
                    }")
                    Text(text = "From user: ${
                        vm.rentalItemState.value.rentalItem.createdByUser.username
                    }")
                    Text(text = "Listed on ${
                        getFormattedTime(vm.rentalItemState.value.rentalItem.createdAt)
                    }")
                }
            }


        }

    }


}

fun getFormattedTime(dateString: String) : String{
    // Parse the date string to LocalDateTime
    val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

    // Format the LocalDateTime to a custom pattern
    return dateTime.format(DateTimeFormatter.ofPattern("d MMM uuuu  HH:mm"))
}