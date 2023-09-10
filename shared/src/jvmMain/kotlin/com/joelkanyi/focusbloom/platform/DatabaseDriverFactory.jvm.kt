package com.joelkanyi.focusbloom.platform

import com.joelkanyi.focusbloom.database.BloomDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            .also { BloomDatabase.Schema.create(it) }
    }
}
