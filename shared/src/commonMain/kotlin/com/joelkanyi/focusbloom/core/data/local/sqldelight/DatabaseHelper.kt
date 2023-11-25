package com.joelkanyi.focusbloom.core.data.local.sqldelight

import app.cash.sqldelight.Query
import com.joelkanyi.focusbloom.database.BloomDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DatabaseHelper(
    private val databaseFlow: StateFlow<BloomDatabase?>,
) {
    private val mutex = Mutex()

    /**
     * Executes [block] with a [Database] instance.
     */
    suspend fun <T> withDatabase(block: suspend (BloomDatabase) -> T) = mutex.withLock {
        block(databaseFlow.first { it != null }!!)
    }

    /**
     * When collected, executes [block] with a [Database] instance.
     */
    fun <T> withDatabaseResult(block: suspend (BloomDatabase) -> T) = flow {
        mutex.withLock {
            emit(block(databaseFlow.first { it != null }!!))
        }
    }

    fun <T : Any> queryAsOneFlow(block: suspend (BloomDatabase) -> Query<T>) =
        withDatabaseResult(block).flatMapLatestAsOne()

    fun <T : Any> queryAsOneOrNullFlow(block: suspend (BloomDatabase) -> Query<T>) =
        withDatabaseResult(block).flatMapLatestAsOneOrNull()

    fun <T : Any> queryAsListFlow(block: suspend (BloomDatabase) -> Query<T>) =
        withDatabaseResult(block).flatMapLatestAsList()
}