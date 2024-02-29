package com.example.edistynytmobiiliohjelmointiprojekti

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.RentalItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalItemsScreen(goBack: () -> Unit) {
    val vm: RentalItemsViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                                 },
                title = { Text(text = vm.rentalItemsState.value.categoryName) },
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
                // Loading indicator
                vm.rentalItemsState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                // In case of error
                vm.rentalItemsState.value.error != null ->
                    Text(text = "error: ${vm.rentalItemsState.value.error}")

                // Draw items
                else -> LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    var itemRow = 0

                    items(vm.rentalItemsState.value.list) {
                        itemRow++
                        val darkerRow = itemRow % 2 == 0

                        TextButton(
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RectangleShape,
                            onClick = { Log.d("rental itemclick", it.rentalItemName) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        if (darkerRow) Color(250, 250, 255)
                                        else Color.White
                                    )
                                    .height(80.dp)
                            ) {

                                // Rental item name
                                // Rental item desc
                                // Rental item serial
                                // Rented by user

                            } // End of items row()
                        }
                    }
                } // End of when
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                FloatingActionButton(
                    onClick = {
                        //vm.showCreateNewRentalItemDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }
        }
    }

}