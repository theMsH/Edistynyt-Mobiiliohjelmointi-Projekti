package com.example.edistynytmobiiliohjelmointiprojekti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiiliohjelmointiprojekti.ui.theme.EdistynytMobiiliohjelmointiProjektiTheme
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.NavigationdrawerViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EdistynytMobiiliohjelmointiProjektiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val navigationdrawerVm: NavigationdrawerViewModel = viewModel()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                                Text(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = "Inventory management",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))
                                

                                // Home / Categories
                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = { Text(text = "Categories") },
                                    selected = navigationdrawerVm.selected.value.selected == "categoriesScreen",
                                    onClick = {
                                        if (navController.currentDestination?.route != "categoriesScreen") {
                                            navigationdrawerVm.setSelected("categoriesScreen")
                                            navController.navigate("categoriesScreen"){
                                                popUpTo("loginScreen") { inclusive = true }
                                            }
                                            scope.launch { drawerState.close() }
                                        }
                                              },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = "Home"
                                        )
                                    }
                                )

                                // Login
                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = { Text(text = "Login") },
                                    selected = navigationdrawerVm.selected.value.selected == "loginScreen",
                                    onClick = {
                                        if (navController.currentDestination?.route != "loginScreen") {
                                            navigationdrawerVm.setSelected("loginScreen")
                                            navController.navigate("loginScreen"){
                                                popUpTo("categoriesScreen") { inclusive = true }
                                            }
                                            scope.launch { drawerState.close() }
                                        }
                                              },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Lock,
                                            contentDescription = "Login"
                                        )
                                    }
                                )
                            }
                        }
                    ) { // Navhost
                        NavHost(
                            navController = navController,
                            startDestination = "loginScreen"
                        ) {
                            composable(route="categoriesScreen") {
                                CategoriesScreen(
                                    onMenuClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    },
                                    onClickEditCategory = {
                                        scope.launch {
                                            navController.navigate("editCategoryScreen/${it.categoryId}")
                                        }
                                    },
                                    onLoginClick = {
                                        scope.launch {
                                            navController.navigate("loginScreen")
                                        }
                                    }
                                )
                            }
                            composable(route="loginScreen") {
                                LoginScreen(
                                    onLoginClick = {
                                        scope.launch {
                                            navController.navigate("categoriesScreen") {
                                                popUpTo("loginScreen") { inclusive = true }
                                            }
                                        }
                                    }
                                )
                            }
                            composable(route="editCategoryScreen/{categoryId}") {
                                EditCategoryScreen(
                                    onArrowClick = {
                                        scope.launch {
                                            navController.navigateUp()
                                        }
                                    }
                                )
                            }
                        }
                    } // Navhost end
                }
            }
        }
    }

}

