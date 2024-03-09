package com.example.edistynytmobiiliohjelmointiprojekti.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.edistynytmobiiliohjelmointiprojekti.CreateNewDialog
import com.example.edistynytmobiiliohjelmointiprojekti.CustomAlert
import com.example.edistynytmobiiliohjelmointiprojekti.R
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.CategoriesViewModel
import java.time.LocalTime


@Composable
fun RandomImage(
    size: Int = 250,
    seed: Int = LocalTime.now().toSecondOfDay()
) {
    val newSeed = seed + 81753
    AsyncImage(
        model = "https://picsum.photos/seed/$newSeed/$size",
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onMenuClick: () -> Unit,
    onClickEditCategory: (CategoryItem, categoriesList: List<String>) -> Unit,
    onLoginClick: () -> Unit,
    openCategory: (CategoryItem) -> Unit
) {
    val vm: CategoriesViewModel = viewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = vm.deleteState.value.done) {
        if (vm.deleteState.value.done) {
            vm.setDeleteDone(false)

            val text =
                if (vm.deleteState.value.success) "Category deleted"
                else "Delete failed: Category has items!"

            val length =
                if (vm.deleteState.value.success) Toast.LENGTH_SHORT
                else Toast.LENGTH_LONG

            Toast.makeText(context, text, length).show()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onMenuClick() }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = { Text(text = "Categories") },
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
                vm.categoriesState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                // In case of error
                vm.categoriesState.value.error != null ->
                    Text(text = "error: ${vm.categoriesState.value.error}")

                // Draw items
                else -> LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    var itemRow = 0

                    items(vm.categoriesState.value.list) {
                        itemRow++
                        val evenRow = itemRow % 2 == 0

                        TextButton(
                            contentPadding = PaddingValues(0.dp),
                            shape = RectangleShape,
                            onClick = { openCategory(it) }
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
                                    RandomImage(300, seed = it.categoryId)
                                }

                                Column {
                                    Row(
                                        modifier = Modifier
                                            .padding(end = 12.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Text(
                                            text = it.categoryName,
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
                                                else vm.setShowDeleteDialog(true)
                                                vm.setSelectedCategoryItem(it)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                tint = colorResource(id = R.color.delete)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                if (authInterceptor.hasEmptyToken()) {
                                                    vm.showUnauthorizedDialog.value = true
                                                }
                                                else onClickEditCategory(it, vm.getNonValidNamesList())
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit",
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
                when {
                    // Create new category dialog
                    vm.createState.value.showDialog -> {
                        CreateNewDialog(
                            onDismiss = { vm.setShowCreateDialog(false) },
                            onConfirm = {
                                vm.postCategory(it)
                                vm.setShowCreateDialog(false)
                                        },
                            notValidNames = vm.getNonValidNamesList(),
                            title = "Create category",
                            placeholder = "New Category"
                        )
                    }

                        // Delete category warning dialog
                    vm.deleteState.value.showDialog -> {
                        com.example.edistynytmobiiliohjelmointiprojekti.DeleteDialog(
                            name = vm.deleteState.value.selectedCategoryItem.categoryName,
                            onDismiss = { vm.setShowDeleteDialog(false) },
                            onConfirm = {
                                vm.deleteCategory(vm.deleteState.value.selectedCategoryItem.categoryId)
                                vm.setShowDeleteDialog(false)
                            }
                        )
                    }

                    // Unauthorized action dialog
                    vm.showUnauthorizedDialog.value -> {
                        CustomAlert(
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

                    vm.deleteState.value.loading || vm.createState.value.loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = {
                        if (authInterceptor.hasEmptyToken()) {
                            vm.showUnauthorizedDialog.value = true
                        } else vm.setShowCreateDialog(true)
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }
        }
    }

}
