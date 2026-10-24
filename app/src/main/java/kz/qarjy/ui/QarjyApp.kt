package kz.qarjy.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kz.qarjy.ui.screens.AddExpenseScreen
import kz.qarjy.ui.screens.HistoryScreen
import kz.qarjy.ui.screens.HomeScreen

/** The three destinations of the app. Routes are plain strings. */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Add : Screen("add")
}

private data class BottomItem(val screen: Screen, val label: String, val emoji: String)

private val bottomItems = listOf(
    BottomItem(Screen.Home, "Home", "🏠"),
    BottomItem(Screen.History, "History", "🧾")
)

@Composable
fun QarjyApp(viewModel: QarjyViewModel = viewModel()) {
    // Lifecycle-aware collection: stops collecting when the app is in the background.
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // rememberNavController keeps the back stack across rotation (it is saved internally).
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // The Add screen is a full-screen form: hide the bottom bar and FAB there.
    val showChrome = currentRoute != Screen.Add.route

    Scaffold(
        bottomBar = {
            if (showChrome) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = { navController.navigateTopLevel(item.screen.route) },
                            icon = { Text(item.emoji) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showChrome) {
                FloatingActionButton(onClick = { navController.navigate(Screen.Add.route) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add expense")
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    state = state,
                    onSeeAll = { navController.navigateTopLevel(Screen.History.route) }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    state = state,
                    onQueryChange = viewModel::onQueryChange,
                    onCategorySelected = viewModel::onCategorySelected,
                    onDelete = viewModel::deleteExpense
                )
            }
            composable(Screen.Add.route) {
                AddExpenseScreen(
                    onSave = { amount, category, note ->
                        viewModel.addExpense(amount, category, note)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

/** Standard bottom-bar navigation: one copy of each tab, state of each tab is restored. */
private fun NavHostController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
