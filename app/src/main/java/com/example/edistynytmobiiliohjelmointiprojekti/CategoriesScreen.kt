package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.CategoriesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onMenuClick: () -> Unit, onClickEditCategory: (CategoryItem) -> Unit) {
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
                .padding(it)
                .padding(20.dp)
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
                else -> LazyColumn(Modifier.fillMaxSize()) {
                    items(categoriesVm.categoriesState.value.list) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Kuva")
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
                                        categoriesVm.deleteCategory(it)
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
                    }
                }
            }

        }
    }

}