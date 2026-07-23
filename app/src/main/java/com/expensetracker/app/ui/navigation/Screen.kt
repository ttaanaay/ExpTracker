package com.expensetracker.app.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Dashboard : Screen("dashboard")
    data object AddEdit : Screen("add_edit?transactionId={transactionId}") {
        fun add() = "add_edit"
        fun edit(transactionId: Long) = "add_edit?transactionId=$transactionId"
    }
    data object Categories : Screen("categories")
    data object Statistics : Screen("statistics")
    data object Calendar : Screen("calendar")
    data object TransactionList : Screen("transactions")
    data object TransactionDetail : Screen("detail/{transactionId}") {
        fun of(transactionId: Long) = "detail/$transactionId"
    }
    data object Search : Screen("search")
    data object Settings : Screen("settings")
}
