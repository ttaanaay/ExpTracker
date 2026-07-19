package com.expensetracker.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.expensetracker.app.ExpenseTrackerApp
import com.expensetracker.app.ui.addedit.AddEditScreen
import com.expensetracker.app.ui.addedit.AddEditViewModel
import com.expensetracker.app.ui.calendar.CalendarScreen
import com.expensetracker.app.ui.calendar.CalendarViewModel
import com.expensetracker.app.ui.category.CategoryScreen
import com.expensetracker.app.ui.category.CategoryViewModel
import com.expensetracker.app.ui.components.AppBottomBar
import com.expensetracker.app.ui.components.BottomTab
import com.expensetracker.app.ui.dashboard.DashboardScreen
import com.expensetracker.app.ui.dashboard.DashboardViewModel
import com.expensetracker.app.ui.detail.TransactionDetailScreen
import com.expensetracker.app.ui.detail.TransactionDetailViewModel
import com.expensetracker.app.ui.search.SearchScreen
import com.expensetracker.app.ui.search.SearchViewModel
import com.expensetracker.app.ui.settings.SettingsScreen
import com.expensetracker.app.ui.settings.SettingsViewModel
import com.expensetracker.app.ui.splash.SplashScreen
import com.expensetracker.app.ui.statistics.StatisticsScreen
import com.expensetracker.app.ui.statistics.StatisticsViewModel
import com.expensetracker.app.ui.transactions.TransactionListScreen
import com.expensetracker.app.ui.transactions.TransactionListViewModel
import com.expensetracker.app.util.GenericViewModelFactory

/** Routes that show the bottom navigation bar + FAB. */
private val topLevelRoutes = setOf(
    Screen.Dashboard.route,
    Screen.TransactionList.route,
    Screen.Statistics.route,
    Screen.Settings.route
)

@Composable
fun AppNavGraph(app: ExpenseTrackerApp) {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(onFinished = { showSplash = false })
        return
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (currentRoute in topLevelRoutes) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onAddClicked = { navController.navigate(Screen.AddEdit.add()) }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                val vm: DashboardViewModel = viewModel(
                    factory = GenericViewModelFactory { DashboardViewModel(app.transactionRepository) }
                )
                DashboardScreen(
                    viewModel = vm,
                    onSeeAllClick = { navController.navigate(Screen.TransactionList.route) },
                    onTransactionClick = { id -> navController.navigate(Screen.TransactionDetail.of(id)) }
                )
            }

            composable(
                route = Screen.AddEdit.route,
                arguments = listOf(navArgument("transactionId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { entry ->
                val idArg = entry.arguments?.getString("transactionId")?.toLongOrNull()
                val vm: AddEditViewModel = viewModel(
                    key = "addedit_${idArg ?: "new"}",
                    factory = GenericViewModelFactory {
                        AddEditViewModel(app.transactionRepository, app.categoryRepository, idArg)
                    }
                )
                AddEditScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }

            composable(Screen.Categories.route) {
                val vm: CategoryViewModel = viewModel(
                    factory = GenericViewModelFactory { CategoryViewModel(app.categoryRepository) }
                )
                CategoryScreen(viewModel = vm, onBack = { navController.popBackStack() })
            }

            composable(Screen.TransactionList.route) {
                val vm: TransactionListViewModel = viewModel(
                    factory = GenericViewModelFactory { TransactionListViewModel(app.transactionRepository) }
                )
                TransactionListScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onSearchClick = { navController.navigate(Screen.Search.route) },
                    onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                    onTransactionClick = { id -> navController.navigate(Screen.TransactionDetail.of(id)) }
                )
            }

            composable(
                route = Screen.TransactionDetail.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("transactionId") ?: 0L
                val vm: TransactionDetailViewModel = viewModel(
                    key = "detail_$id",
                    factory = GenericViewModelFactory { TransactionDetailViewModel(app.transactionRepository, id) }
                )
                TransactionDetailScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onEdit = { editId -> navController.navigate(Screen.AddEdit.edit(editId)) },
                    onDeleted = { navController.popBackStack() }
                )
            }

            composable(Screen.Calendar.route) {
                val vm: CalendarViewModel = viewModel(
                    factory = GenericViewModelFactory { CalendarViewModel(app.transactionRepository) }
                )
                CalendarScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onTransactionClick = { id -> navController.navigate(Screen.TransactionDetail.of(id)) }
                )
            }

            composable(Screen.Statistics.route) {
                val vm: StatisticsViewModel = viewModel(
                    factory = GenericViewModelFactory {
                        StatisticsViewModel(app.transactionRepository, app.categoryRepository)
                    }
                )
                StatisticsScreen(viewModel = vm)
            }

            composable(Screen.Search.route) {
                val vm: SearchViewModel = viewModel(
                    factory = GenericViewModelFactory { SearchViewModel(app.transactionRepository) }
                )
                SearchScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },
                    onTransactionClick = { id -> navController.navigate(Screen.TransactionDetail.of(id)) }
                )
            }

            composable(Screen.Settings.route) {
                val vm: SettingsViewModel = viewModel(
                    factory = GenericViewModelFactory {
                        SettingsViewModel(app.settingsDataStore, app.transactionRepository)
                    }
                )
                SettingsScreen(
                    viewModel = vm,
                    onManageCategories = { navController.navigate(Screen.Categories.route) }
                )
            }
        }
    }
}
