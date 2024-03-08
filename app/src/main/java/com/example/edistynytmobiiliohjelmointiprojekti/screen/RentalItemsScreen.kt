package com.example.edistynytmobiiliohjelmointiprojekti.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.CreateNewDialog
import com.example.edistynytmobiiliohjelmointiprojekti.MyAlert
import com.example.edistynytmobiiliohjelmointiprojekti.R
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.RentalItemsViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentalItemsScreen(
    goBack: () -> Unit,
    goToEditRentalItemScreen: (Int, Int, String) -> Unit,
    goToRentalItemScreen: (Int, Int, String) -> Unit,
    onLoginClick: () -> Unit
) {
    val vm: RentalItemsViewModel = viewModel()

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
                title = { Text(text = vm.rentalItemsState.value.categoryName) },
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
                        val evenRow = itemRow % 2 == 0

                        TextButton(
                            contentPadding = PaddingValues(0.dp),
                            shape = RectangleShape,
                            onClick = {
                                Log.d("rental itemclick", it.rentalItemName)
                                goToRentalItemScreen(it.rentalItemId, vm.categoryId, vm.categoryName)
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color =
                                        if (evenRow) colorResource(R.color.row_uneven)
                                        else MaterialTheme.colorScheme.background
                                    )
                                    .height(80.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(start = 10.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    // Replace with real image from database
                                    RandomImage(200, it.rentalItemId)
                                }
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = it.rentalItemName,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (authInterceptor.hasEmptyToken()) {
                                                   vm.showUnauthorizedDialog.value = true
                                                }
                                                else {
                                                    goToEditRentalItemScreen(
                                                        it.rentalItemId,
                                                        vm.categoryId,
                                                        vm.categoryName
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Edit,
                                                contentDescription = "Edit item",
                                                tint = colorResource(id = R.color.edit)
                                            )
                                        }
                                    }
                                }
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
                        if (authInterceptor.hasEmptyToken()) {
                            vm.showUnauthorizedDialog.value = true
                        }
                        else vm.showCreateNewRentalItemDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }

                // Create new category dialog
                if (vm.showCreateNewRentalItemDialog.value) {
                    CreateNewDialog(
                        showCreateNewDialog = vm.showCreateNewRentalItemDialog,
                        onConfirm = { vm.postNewItem(it) },
                        title = "Create item",
                        placeholder = "New Item"
                    )
                }

                // Unauthorized action dialog
                if (vm.showUnauthorizedDialog.value) {
                    MyAlert(
                        onDismissRequest = { vm.showUnauthorizedDialog.value = false },
                        onConfirmation = {
                            onLoginClick()
                            vm.showUnauthorizedDialog.value = false
                        },
                        dialogTitle = "Unauthorized",
                        dialogText = "Please login to perform this action",
                        icon = Icons.Default.Lock,
                        confirmButtonText = "Login",
                        dismissButtonText = "Dismiss"
                    )
                }
            }
        }
    }

}

