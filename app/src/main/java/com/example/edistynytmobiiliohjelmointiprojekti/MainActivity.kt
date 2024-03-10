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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.edistynytmobiiliohjelmointiprojekti.api.authInterceptor
import com.example.edistynytmobiiliohjelmointiprojekti.database.AccessToken
import com.example.edistynytmobiiliohjelmointiprojekti.database.DatabaseProvider
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
                    val loggedIn: MutableState<Boolean?> = rememberSaveable { mutableStateOf(null) }
                    val db = DatabaseProvider.getInstance(applicationContext)


                    LaunchedEffect(key1 = loggedIn.value){
                        Log.d("MainActivity()", " LaunchedEffect Start")

                        if (loggedIn.value == null) {
                            Log.d("MainActivity()", " LaunchedEffect: null")
                            val query = db.accessTokenDao().getAccessToken()

                            if (query != null) {
                                Log.d("MainActivity()", " LaunchedEffect: token found and updated, navigate categoriesScreen")
                                authInterceptor.updateToken(query.accessToken)

                                navController.navigate("categoriesScreen") {
                                    popUpTo("loginScreen") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                        else if (loggedIn.value == false) {
                            Log.d("MainActivity()", " LaunchedEffect: logout")
                            authInterceptor.updateToken("")
                            db.accessTokenDao().clearAccessToken()

                            navController.navigate("loginScreen") {
                                launchSingleTop = true
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

                                if (authInterceptor.hasEmptyToken()) {
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
                                else {
                                    // Logout
                                    NavigationDrawerItem(
                                        modifier = Modifier
                                            .padding(NavigationDrawerItemDefaults.ItemPadding),
                                        label = {
                                            Text(text = stringResource(R.string.logout))
                                        },
                                        selected = navBackStackEntry?.destination?.route == "loginScreen",
                                        onClick = {
                                            loggedIn.value = false
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
                                                db.accessTokenDao().insert(AccessToken(accessToken = it))
                                                Log.d("onLoginSuccess", "accessToken insert")
                                            }
                                        }
                                        navController.navigate("categoriesScreen") {
                                            popUpTo("loginScreen") { inclusive = true }
                                            launchSingleTop = true
                                        }
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

