package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.CategoriesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(onMenuClick: () -> Unit, onClickEditCategory: (CategoryItem) -> Unit) {
    val categoriesVm: CategoriesViewModel = viewModel()
    val showDeleteDialog = remember{ mutableStateOf(false) }
    val showCreateCategoryDialog = remember{ mutableStateOf(false) }
    var selectedCategoryItem: CategoryItem? = null

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
                else -> LazyColumn(
                    Modifier.fillMaxSize()
                ) {
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
                                        showDeleteDialog.value = true
                                        selectedCategoryItem = it
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
                            if (showDeleteDialog.value && selectedCategoryItem == it) {
                                DeleteDialog(
                                    showDeleteDialog = showDeleteDialog,
                                    categoryName = it.categoryName,
                                    onConfirm = { categoriesVm.deleteCategory(it) }
                                )
                            }

                        }
                    }
                    item{
                        if (showCreateCategoryDialog.value) {
                            CreateCategoryDialog(
                                showCreateCategoryDialog = showCreateCategoryDialog,
                                categoriesVm = categoriesVm
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom) {
                FloatingActionButton(
                    onClick = {
                        showCreateCategoryDialog.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "Floating action button.")
                }
            }
        }
    }

}

@Composable
fun DeleteDialog(showDeleteDialog: MutableState<Boolean>, categoryName: String, onConfirm: () -> Unit) {

    Dialog(onDismissRequest = { showDeleteDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.scale(2f),
                    tint = Color.Red,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Delete $categoryName",
                    modifier = Modifier.padding(10.dp, 26.dp, 10.dp, 26.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                        }
                    ) {
                        Text(style = MaterialTheme.typography.bodyLarge, text = "Cancel")
                    }
                    TextButton(
                        onClick = {
                            showDeleteDialog.value = false
                            onConfirm()
                        }
                    ) {
                        Text(style = MaterialTheme.typography.bodyLarge, text = "Delete", color = Color.Red)
                    }
                }
            }
        }
    }

}

@Composable
fun CreateCategoryDialog(showCreateCategoryDialog: MutableState<Boolean>, categoriesVm: CategoriesViewModel) {

    Dialog(onDismissRequest = { showCreateCategoryDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Create category",
                    modifier = Modifier.padding(10.dp, 26.dp, 10.dp, 0.dp),
                )
                TextField(
                    modifier = Modifier.padding(20.dp, 30.dp),
                    value = categoriesVm.categoryState.value.categoryName,
                    placeholder = { Text(text = "New Category") },
                    onValueChange = {
                        categoriesVm.setCategoryName(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (categoriesVm.categoryState.value.categoryName != "") {
                                categoriesVm.setCategoryName("")
                                showCreateCategoryDialog.value = false
                                categoriesVm.postCategory()
                            }
                        }
                    )
                )
                Button(
                    enabled = categoriesVm.categoryState.value.categoryName != "",
                    onClick = {
                        categoriesVm.setCategoryName("")
                        showCreateCategoryDialog.value = false
                        categoriesVm.postCategory()
                    }
                ) {
                    Text(style = MaterialTheme.typography.bodyLarge, text = "Add")
                }
            }
        }
    }

}

