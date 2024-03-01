package com.example.edistynytmobiiliohjelmointiprojekti

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.CategoriesViewModel
import java.time.LocalDateTime


@Composable
fun RandomImage(size: Int = 250) {
    AsyncImage(
        model = "https://picsum.photos/seed/${LocalDateTime.now()}/$size",
        contentDescription = null
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onMenuClick: () -> Unit,
    onClickEditCategory: (CategoryItem) -> Unit,
    onLoginClick: () -> Unit,
    openCategory: (CategoryItem) -> Unit
) {

    val categoriesVm: CategoriesViewModel = viewModel()

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
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when {
                // Loading indicator
                categoriesVm.categoriesState.value.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                // In case of error
                categoriesVm.categoriesState.value.error != null ->
                    Text(text = "error: ${categoriesVm.categoriesState.value.error}")

                // Draw items
                else -> LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    var itemRow = 0

                    items(categoriesVm.categoriesState.value.list) {
                        itemRow++
                        val evenRow = itemRow % 2 == 0

                        TextButton(
                            contentPadding = PaddingValues(vertical = 4.dp),
                            shape = RectangleShape,
                            onClick = { openCategory(it) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color =
                                        if (evenRow) MaterialTheme.colorScheme.background
                                        else colorResource(R.color.row_uneven)
                                    )
                                    .height(80.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(start = 10.dp)
                                ) {
                                    // Replace with real image from database
                                    RandomImage(300)
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
                                                categoriesVm.showDeleteDialog.value = true
                                                categoriesVm.selectedCategoryItem.value = it
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
                                                onClickEditCategory(it)
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
                FloatingActionButton(
                    onClick = {
                        categoriesVm.showCreateNewDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }

                // Create new category dialog
                if (categoriesVm.showCreateNewDialog.value) {
                    CreateNewCategoryDialog(
                        showCreateNewDialog = categoriesVm.showCreateNewDialog,
                        onConfirm = { categoriesVm.postCategory(it)}
                    )
                }

                // Delete category warning dialog
                if (categoriesVm.showDeleteDialog.value) {
                    DeleteDialog(
                        showDeleteDialog = categoriesVm.showDeleteDialog,
                        categoryName = categoriesVm.selectedCategoryItem.value.categoryName,
                        onConfirm = { categoriesVm.deleteCategory(categoriesVm.selectedCategoryItem.value.categoryId) }
                    )
                }
/*
                // Unauthorized action dialog
                if (categoriesVm.showUnauthorizedDialog.value) {
                    MyAlert(
                        onDismissRequest = { categoriesVm.showUnauthorizedDialog.value = false },
                        onConfirmation = {
                            onLoginClick()
                            categoriesVm.showUnauthorizedDialog.value = false
                                         },
                        dialogTitle = "Unauthorized",
                        dialogText = "Please login to perform this action",
                        icon = Icons.Default.Lock,
                        confirmButtonText = "Login",
                        dismissButtonText = "Dismiss"
                    )
                }
*/
            }
        }
    }

}



