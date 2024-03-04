package com.example.edistynytmobiiliohjelmointiprojekti

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiiliohjelmointiprojekti.screen.CategoriesScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.EditCategoryScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.EditRentalItemScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.LoginScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.RegisterScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.RentalItemScreen
import com.example.edistynytmobiiliohjelmointiprojekti.screen.RentalItemsScreen
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
                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(modifier = Modifier) {
                                Text(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = "Rental App",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))

                                // Home / Categories
                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = { Text(text = "Categories") },
                                    selected = navBackStackEntry?.destination?.route == "categoriesScreen",
                                    onClick = {
                                        navController.navigate("categoriesScreen"){
                                            popUpTo("loginScreen") { inclusive = true }
                                            launchSingleTop = true
                                        }
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
                                    selected = navBackStackEntry?.destination?.route == "loginScreen",
                                    onClick = {
                                        navController.navigate("loginScreen"){
                                            popUpTo("categoriesScreen") { inclusive = false }
                                            launchSingleTop = true
                                        }
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
                                        navController.navigate("editCategoryScreen/${it.categoryId}")

                                    },
                                    onLoginClick = {
                                        navController.navigate("loginScreen")
                                    },
                                    openCategory = {
                                        navController.navigate(
                                            "rentalItemScreen/${it.categoryId}/${it.categoryName}"
                                        )
                                    },
                                    deleteToast = { deleted ->
                                        val text =
                                            if (deleted) "Category deleted"
                                            else "Delete failed: Category has items!"
                                        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            composable(route="loginScreen") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        navController.navigate("categoriesScreen") {
                                            popUpTo("loginScreen") { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    },
                                    onLoginFail = {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "User or password is not valid",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onRegisterClick = {
                                        navController.navigate("registerScreen")
                                    }
                                )
                            }
                            composable(route="editCategoryScreen/{categoryId}") {
                                EditCategoryScreen(
                                    goBack = {
                                        navController.navigateUp()
                                    },
                                    goToCategoriesScreen = {
                                        navController.navigate("categoriesScreen") {
                                            popUpTo("categoriesScreen") { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(route = "rentalItemScreen/{categoryId}/{categoryName}") {
                                RentalItemsScreen(
                                    goBack = {
                                        navController.navigateUp()
                                    },
                                    goToEditRentalItemScreen = { rentalItemId, categoryId, categoryName ->
                                        navController.navigate("editRentalItemScreen" +
                                                    "/${rentalItemId}" +
                                                    "/${categoryId}" +
                                                    "/${categoryName}"
                                        )
                                    },
                                    goToRentalItemScreen = { rentalItemId, categoryId, categoryName ->
                                        navController.navigate("rentalItemScreen" +
                                                "/${rentalItemId}" +
                                                "/${categoryId}" +
                                                "/${categoryName}"
                                        )
                                    }
                                )
                            }
                            composable(route = "editRentalItemScreen/{rentalItemId}/{categoryItemId}/{categoryItemName}") {
                                EditRentalItemScreen (
                                    goBack = {
                                        navController.navigateUp()
                                    },
                                    goToRentalItemsScreen = { categoryId, categoryName ->
                                        navController.navigate("rentalItemScreen" +
                                                "/${categoryId}" +
                                                "/${categoryName}"
                                        ) {
                                            popUpTo("rentalItemScreen" +
                                                    "/${categoryId}" +
                                                    "/${categoryName}"
                                            ) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }
                            composable(route = "rentalItemScreen/{rentalItemId}/{categoryItemId}/{categoryItemName}") {
                                RentalItemScreen(
                                    goBack = {
                                        navController.navigateUp()
                                    },
                                    goToEditRentalItemScreen = { rentalItemId, categoryId, categoryName ->
                                        navController.navigate("editRentalItemScreen" +
                                                "/${rentalItemId}" +
                                                "/${categoryId}" +
                                                "/${categoryName}"
                                        )
                                    }
                                )
                            }

                            composable(route = "registerScreen") {
                                RegisterScreen(
                                    onRegisterClick = {
                                        navController.navigateUp()
                                        Toast.makeText(
                                            this@MainActivity,
                                            "New account registered!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    goBack = {
                                        navController.navigateUp()
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

