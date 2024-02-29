package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun CategoriesScreen(onMenuClick: () -> Unit, onClickEditCategory: (CategoryItem) -> Unit, onLoginClick: () -> Unit) {
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
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it).padding(bottom = 16.dp)
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
                        val darkerRow = itemRow % 2 == 0

                        Row(modifier = Modifier
                            .background(
                                if (darkerRow) Color(250,250,255)
                                else Color.White
                            )
                            .height(80.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(4.dp, 4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Replace with real image from database
                                RandomImage(300)
                            }

                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 16.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = it.categoryName,
                                        style = MaterialTheme.typography.headlineLarge
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
                                        Icon(imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(220,0,0)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            onClickEditCategory(it)
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color(0,100,0)
                                        )
                                    }
                                }

                            }
                        } // End of items row()
                    }
                } // End of when
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                FloatingActionButton(
                    onClick = {
                        categoriesVm.showCreateCategoryDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }

                // Create new category dialog
                if (categoriesVm.showCreateCategoryDialog.value) {
                    CreateCategoryDialog(
                        showCreateCategoryDialog = categoriesVm.showCreateCategoryDialog,
                        categoriesVm = categoriesVm
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
                // Unauthorized action dialog ( Planned for login )
                if () {
                    MyAlert(
                        onDismissRequest = {},
                        onConfirmation = { onLoginClick() },
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



