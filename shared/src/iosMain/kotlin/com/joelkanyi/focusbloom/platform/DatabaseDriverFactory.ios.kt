package com.joelkanyi.focusbloom.platform

import com.joelkanyi.focusbloom.database.BloomDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(schema = BloomDatabase.Schema, name = "bloom.db")
    }
}
