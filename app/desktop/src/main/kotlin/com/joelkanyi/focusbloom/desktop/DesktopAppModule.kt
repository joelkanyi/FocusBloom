/*
 * Copyright 2026 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.desktop

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.joelkanyi.focusbloom.core.database.FocusBloomDatabase
import com.joelkanyi.focusbloom.core.database.FocusBloomSyncSchema
import com.joelkanyi.focusbloom.core.database.createFocusBloomDatabase
import com.joelkanyi.focusbloom.core.datastore.FocusBloomSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.PreferencesSettings
import org.koin.dsl.module
import java.io.File
import java.util.prefs.Preferences

/**
 * The Desktop composition root's dependency graph. Binds a Java Preferences settings store and a
 * JDBC SQLite driver over a file in the user's home directory. The JdbcSqliteDriver schema
 * argument creates or migrates the database automatically.
 */
val desktopAppModule = module {
    single<ObservableSettings> {
        PreferencesSettings(Preferences.userRoot().node("com/joelkanyi/focusbloom"))
    }
    single { FocusBloomSettings(get()) }

    single<SqlDriver> {
        val dbFile = File(System.getProperty("user.home"), ".focusbloom/focusbloom.db")
        dbFile.parentFile?.mkdirs()
        JdbcSqliteDriver(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            schema = FocusBloomSyncSchema,
        )
    }
    single<FocusBloomDatabase> { createFocusBloomDatabase(get()) }
}
