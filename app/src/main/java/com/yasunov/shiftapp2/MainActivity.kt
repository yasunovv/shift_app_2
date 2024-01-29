package com.yasunov.shiftapp2

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yasunov.shiftapp2.person.PersonScreen
import com.yasunov.shiftapp2.person.PersonViewModel
import com.yasunov.shiftapp2.person.di.ViewModelFactoryProvider
import com.yasunov.shiftapp2.ui.theme.ShiftApp2Theme
import com.yasunov.shiftapp2.users.UsersScreen
import com.yasunov.shiftapp2.users.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShiftApp2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val scrollBehavior =
                        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    Scaffold(
                        topBar = {
                            if (navBackStackEntry?.destination?.route?.contains(getString(R.string.profile)) == true) {
                                TopAppBar(
                                    title = {
                                        CenterAlignedTopAppBar(
                                            title = {
                                                Text(
                                                    "Карточка пользователя",
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            },
                                            navigationIcon = {
                                                IconButton(onClick = {
                                                    navController.popBackStack()
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Filled.ArrowBack,
                                                        contentDescription = "Localized description"
                                                    )
                                                }
                                            },
                                            scrollBehavior = scrollBehavior,
                                        )
                                    })
                            }
                        },
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = getString(R.string.users),
                            route = getString(R.string.root)
                        ) {
                            composable(getString(R.string.users)) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry(getString(R.string.root))
                                }
                                UsersScreen(
                                    modifier = Modifier.padding(paddingValues),
                                    usersViewModel = hiltViewModel<UsersViewModel>(parentEntry),
                                    onItemClicked = { id ->
                                        navController.navigate("${getString(R.string.profile)}/$id")
                                    }
                                )
                            }
                            composable(
                                "${getString(R.string.profile)}/{id}",
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.IntType
                                    }
                                )
                            ) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry(getString(R.string.root))
                                }
                                val factory = EntryPointAccessors.fromActivity(
                                    LocalContext.current as Activity,
                                    ViewModelFactoryProvider::class.java
                                ).personViewModelFactory()
                                val id = backStackEntry.arguments?.getInt("id")!!
                                val personViewModel: PersonViewModel = viewModel(
                                    viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                                        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
                                    },
                                    factory = PersonViewModel.provideMainViewModelFactory(
                                        factory,
                                        id
                                    ),
                                )
                                PersonScreen(
                                    personViewModel = personViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
