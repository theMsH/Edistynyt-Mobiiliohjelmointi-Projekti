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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiiliohjelmointiprojekti.ui.theme.EdistynytMobiiliohjelmointiProjektiTheme
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
                    
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                                Spacer(modifier = Modifier.height(16.dp))

                                // Home / Categories
                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = { Text(text = "Categories") },
                                    selected = navController.currentDestination?.route == "categoriesScreen",
                                    onClick = {
                                        navController.navigate("categoriesScreen")
                                        scope.launch { drawerState.close() }
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
                                    selected = navController.currentDestination?.route == "loginScreen",
                                    onClick = {
                                        navController.navigate("loginScreen")
                                        scope.launch { drawerState.close() }
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
                                            navController.navigate("categoryEditScreen/${it.categoryId}")
                                        }
                                    }
                                )
                            }
                            composable(route="loginScreen") {
                                LoginScreen(
                                    onLoginClick = {
                                        scope.launch {
                                            navController.navigate("categoriesScreen")
                                        }
                                    }
                                )
                            }
                            composable(route="categoryEditScreen/{categoryId}" ) {
                                EditCategoryScreen()
                            }
                        }
                    } // Navhost end
                }
            }
        }
    }
}
