package com.example.edistynytmobiiliohjelmointiprojekti.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.R
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditRentalItemViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalItemScreen(
    goBack: () -> Unit,
    goToEditRentalItemScreen: (Int, Int, String) -> Unit
) {
    val vm: EditRentalItemViewModel = viewModel()
    val configuration = LocalConfiguration.current

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
                ),
                actions = {
                    IconButton(
                        onClick = {
                            goToEditRentalItemScreen(
                                vm.rentalItemState.value.rentalItem.rentalItemId,
                                vm.rentalItemState.value.rentalItem.category.categoryId,
                                vm.rentalItemState.value.rentalItem.category.categoryName
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit item",
                            tint = colorResource(id = R.color.edit)
                        )
                    }
                }
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
                    Text(text = "error: ${vm.rentalItemState.value.error}")


                // Orientation: LandScape
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        RandomImage(800, vm.rentalItemState.value.rentalItem.rentalItemId)
                    }
                    Spacer(modifier = Modifier.width(100.dp))

                    Column(
                        Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(34.dp))
                        Text(text = "Listed on ${getFormattedTime(vm.rentalItemState.value.rentalItem.createdAt)}")
                        Text(text = "by ${vm.rentalItemState.value.rentalItem.createdByUser.username}")
                        Spacer(modifier = Modifier.height(34.dp))
                        Text(text = "Status: ${vm.rentalItemState.value.rentalItem.rentalState.rentalState}")
                    }
                }


                // Orientation: Portrait
                else -> Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RandomImage(600, vm.rentalItemState.value.rentalItem.rentalItemId)
                    Spacer(modifier = Modifier.height(34.dp))
                    Text(text = "Listed on ${getFormattedTime(vm.rentalItemState.value.rentalItem.createdAt)}")
                    Text(text = "by ${vm.rentalItemState.value.rentalItem.createdByUser.username}")
                    Spacer(modifier = Modifier.height(34.dp))
                    Text(text = "Status: ${vm.rentalItemState.value.rentalItem.rentalState.rentalState}")
                }
            }

        }
    }

}

fun getFormattedTime(dateString: String) : String{
    // Parse the date string to LocalDateTime
    val dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

    // Format the LocalDateTime to a custom pattern
    val date = dateTime.format(DateTimeFormatter.ofPattern("d MMM uuuu"))
    val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    val formattedTime = "$date at $time"

    return formattedTime
}

