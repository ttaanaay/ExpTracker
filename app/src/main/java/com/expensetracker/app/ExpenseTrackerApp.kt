package com.expensetracker.app

import android.app.Application
import com.expensetracker.app.data.local.AppDatabase
import com.expensetracker.app.data.repository.CategoryRepository
import com.expensetracker.app.data.repository.TransactionRepository
import com.expensetracker.app.datastore.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ExpenseTrackerApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database: AppDatabase by lazy { AppDatabase.getInstance(this, applicationScope) }
    val categoryRepository: CategoryRepository by lazy { CategoryRepository(database.categoryDao()) }
    val transactionRepository: TransactionRepository by lazy { TransactionRepository(database.transactionDao()) }
    val settingsDataStore: SettingsDataStore by lazy { SettingsDataStore(this) }
}
