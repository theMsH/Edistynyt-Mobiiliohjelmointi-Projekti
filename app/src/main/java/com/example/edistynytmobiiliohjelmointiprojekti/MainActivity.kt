package com.example.edistynytmobiiliohjelmointiprojekti

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.LockOpen
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiiliohjelmointiprojekti.database.AccessToken
import com.example.edistynytmobiiliohjelmointiprojekti.database.DbProvider.db
import com.example.edistynytmobiiliohjelmointiprojekti.ui.theme.EdistynytMobiiliohjelmointiProjektiTheme
import com.example.edistynytmobiiliohjelmointiprojekti.view.CategoriesScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.EditCategoryScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.EditRentalItemScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.LoginScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.RegisterScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.RentalItemScreen
import com.example.edistynytmobiiliohjelmointiprojekti.view.RentalItemsScreen
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.MainActivityViewModel
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
                    val vm: MainActivityViewModel = viewModel()

                    LaunchedEffect(key1 = vm.loginState.value.done){
                        if (vm.loginState.value.done) {
                            vm.setDone(false)

                            if (vm.loginState.value.loggedIn) {
                                navController.navigate("categoriesScreen") {
                                    popUpTo("loginScreen") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(modifier = Modifier) {
                                Text(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = stringResource(R.string.drawer_menu_title),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))

                                // Home / Categories
                                NavigationDrawerItem(
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    label = { Text(text = stringResource(R.string.categories)) },
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

                                if (vm.loginState.value.loggedIn) {
                                    // Logout
                                    NavigationDrawerItem(
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding),
                                        label = {
                                            Text(text = stringResource(R.string.logout))
                                        },
                                        selected = navBackStackEntry?.destination?.route == "loginScreen",
                                        onClick = {
                                            navController.navigate("loginScreen"){
                                                launchSingleTop = true
                                            }
                                            vm.logout()
                                            scope.launch { drawerState.close() }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.LockOpen,
                                                contentDescription = "Logout"
                                            )
                                        }
                                    )
                                }
                                else {
                                    // Login
                                    NavigationDrawerItem(
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding),
                                        label = {
                                            Text(text = stringResource(R.string.login))
                                        },
                                        selected = navBackStackEntry?.destination?.route == "loginScreen",
                                        onClick = {
                                            navController.navigate("loginScreen"){
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
                        }
                    ) { // NavHost
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
                                    onClickEditCategory = { categoryItem, nonValidNamesList ->
                                        navController.navigate("editCategoryScreen" +
                                                "/${categoryItem.categoryId}" +
                                                "/${nonValidNamesList}")

                                    },
                                    onLoginClick = {
                                        navController.navigate("loginScreen")
                                    },
                                    openCategory = {
                                        navController.navigate(
                                            "rentalItemScreen/${it.categoryId}/${it.categoryName}"
                                        )
                                    }
                                )
                            }
                            composable(route="loginScreen") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        scope.launch {
                                            if (it != null) {
                                                db.accessTokenDao().insertToken(AccessToken(accessToken = it))
                                                Log.d("onLoginSuccess", "accessToken insert")
                                            }
                                        }
                                        navController.navigate("categoriesScreen") {
                                            popUpTo("loginScreen") { inclusive = true }
                                            launchSingleTop = true
                                        }
                                        vm.login()
                                    },
                                    onRegisterClick = {
                                        navController.navigate("registerScreen")
                                    }
                                )
                            }
                            composable(route="editCategoryScreen/{categoryId}/{nonValidNamesList}") {
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
                                    goToEditRentalItemScreen = { rentalItemId, categoryItem ->
                                        navController.navigate("editRentalItemScreen" +
                                                    "/${rentalItemId}" +
                                                    "/${categoryItem.categoryId}" +
                                                    "/${categoryItem.categoryName}"
                                        )
                                    },
                                    goToRentalItemScreen = { rentalItemId, categoryItem ->
                                        navController.navigate("rentalItemScreen" +
                                                "/${rentalItemId}" +
                                                "/${categoryItem.categoryId}" +
                                                "/${categoryItem.categoryName}"
                                        )
                                    },
                                    onLoginClick = {
                                        navController.navigate("loginScreen")
                                    }
                                )
                            }
                            composable(route = "editRentalItemScreen/{rentalItemId}/{categoryItemId}/{categoryItemName}") {
                                EditRentalItemScreen (
                                    goBack = {
                                        navController.navigateUp()
                                    },
                                    goToRentalItemsScreen = { categoryItem ->
                                        navController.navigate("rentalItemScreen" +
                                                "/${categoryItem.categoryId}" +
                                                "/${categoryItem.categoryName}"
                                        ) {
                                            popUpTo("rentalItemScreen" +
                                                    "/${categoryItem.categoryId}" +
                                                    "/${categoryItem.categoryName}"
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
                                    goToEditRentalItemScreen = { rentalItemId, categoryItem ->
                                        navController.navigate("editRentalItemScreen" +
                                                "/${rentalItemId}" +
                                                "/${categoryItem.categoryId}" +
                                                "/${categoryItem.categoryName}"
                                        )
                                    },
                                    onLoginClick = {
                                        navController.navigate("loginScreen")
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
                    } // NavHost end
                }
            }
        }
    }

}

