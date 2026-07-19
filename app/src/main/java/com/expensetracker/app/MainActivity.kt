package com.expensetracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.expensetracker.app.datastore.AppThemeMode
import com.expensetracker.app.ui.navigation.AppNavGraph
import com.expensetracker.app.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ExpenseTrackerApp

        setContent {
            val settings by app.settingsDataStore.settingsFlow.collectAsState(
                initial = com.expensetracker.app.datastore.AppSettings()
            )

            ExpenseTrackerTheme(themeMode = settings.themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph(app = app)
                }
            }
        }
    }
}
