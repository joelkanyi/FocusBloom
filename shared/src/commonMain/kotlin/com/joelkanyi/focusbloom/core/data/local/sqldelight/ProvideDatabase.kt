package com.joelkanyi.focusbloom.core.data.local.sqldelight

import com.joelkanyi.focusbloom.core.data.adapter.colorAdapter
import com.joelkanyi.focusbloom.core.data.adapter.consumedFocusTimeAdapter
import com.joelkanyi.focusbloom.core.data.adapter.consumedLongBreakTimeAdapter
import com.joelkanyi.focusbloom.core.data.adapter.consumedShortBreakTimeAdapter
import com.joelkanyi.focusbloom.core.data.adapter.currentAdapter
import com.joelkanyi.focusbloom.core.data.adapter.currentCycleAdapter
import com.joelkanyi.focusbloom.core.data.adapter.focusSessionsAdapter
import com.joelkanyi.focusbloom.core.data.adapter.idAdapter
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.joelkanyi.focusbloom.platform.DatabaseDriverFactory
import database.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProvideDatabase(
    private val databaseDriverFactory: DatabaseDriverFactory,
) {
    private suspend fun createDatabase(): BloomDatabase {
        val driver = databaseDriverFactory.provideDbDriver(BloomDatabase.Schema)
        val database = BloomDatabase(
            driver,
            taskEntityAdapter = TaskEntity.Adapter(
                idAdapter = idAdapter,
                colorAdapter = colorAdapter,
                consumedFocusTimeAdapter = consumedFocusTimeAdapter,
                consumedLongBreakTimeAdapter = consumedLongBreakTimeAdapter,
                consumedShortBreakTimeAdapter = consumedShortBreakTimeAdapter,
                currentAdapter = currentAdapter,
                currentCycleAdapter = currentCycleAdapter,
                focusSessionsAdapter = focusSessionsAdapter,
            ),
        )
        return database
    }

    fun provideDatabaseFlow(): StateFlow<BloomDatabase?> {
        val stateFlow = MutableStateFlow<BloomDatabase?>(null)
        CoroutineScope(Dispatchers.Main).launch {
            createDatabase().apply { stateFlow.update { this } }
        }
        return stateFlow
    }
}
