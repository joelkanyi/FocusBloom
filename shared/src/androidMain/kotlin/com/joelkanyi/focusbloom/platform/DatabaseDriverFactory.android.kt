package com.joelkanyi.focusbloom.platform

import android.content.Context
import com.joelkanyi.focusbloom.database.BloomDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(BloomDatabase.Schema, context, "bloom.db")
    }
}
