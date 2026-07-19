package com.expensetracker.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.navigation.Screen

enum class BottomTab(val route: String, val label: String) {
    DASHBOARD(Screen.Dashboard.route, "หน้าหลัก"),
    TRANSACTIONS(Screen.TransactionList.route, "รายการ"),
    STATISTICS(Screen.Statistics.route, "สถิติ"),
    SETTINGS(Screen.Settings.route, "ตั้งค่า")
}

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onTabSelected: (BottomTab) -> Unit,
    onAddClicked: () -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = currentRoute == BottomTab.DASHBOARD.route,
            onClick = { onTabSelected(BottomTab.DASHBOARD) },
            icon = { Icon(Icons.Filled.Home, contentDescription = BottomTab.DASHBOARD.label) },
            label = { androidx.compose.material3.Text(BottomTab.DASHBOARD.label) }
        )
        NavigationBarItem(
            selected = currentRoute == BottomTab.TRANSACTIONS.route,
            onClick = { onTabSelected(BottomTab.TRANSACTIONS) },
            icon = { Icon(Icons.Filled.Receipt, contentDescription = BottomTab.TRANSACTIONS.label) },
            label = { androidx.compose.material3.Text(BottomTab.TRANSACTIONS.label) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onAddClicked,
            icon = {
                FloatingActionButton(
                    onClick = onAddClicked,
                    modifier = Modifier.size(40.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "เพิ่มรายการ")
                }
            },
            label = { }
        )
        NavigationBarItem(
            selected = currentRoute == BottomTab.STATISTICS.route,
            onClick = { onTabSelected(BottomTab.STATISTICS) },
            icon = { Icon(Icons.Filled.PieChart, contentDescription = BottomTab.STATISTICS.label) },
            label = { androidx.compose.material3.Text(BottomTab.STATISTICS.label) }
        )
        NavigationBarItem(
            selected = currentRoute == BottomTab.SETTINGS.route,
            onClick = { onTabSelected(BottomTab.SETTINGS) },
            icon = { Icon(Icons.Filled.Settings, contentDescription = BottomTab.SETTINGS.label) },
            label = { androidx.compose.material3.Text(BottomTab.SETTINGS.label) }
        )
    }
}
