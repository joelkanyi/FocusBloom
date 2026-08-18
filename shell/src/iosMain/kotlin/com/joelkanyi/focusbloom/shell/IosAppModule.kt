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
package com.joelkanyi.focusbloom.shell

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.joelkanyi.focusbloom.core.database.FocusBloomDatabase
import com.joelkanyi.focusbloom.core.database.focusBloomSyncSchema
import com.joelkanyi.focusbloom.core.database.createFocusBloomDatabase
import com.joelkanyi.focusbloom.core.datastore.FocusBloomSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * The iOS composition root's dependency graph. Binds an NSUserDefaults settings store and the
 * native SQLite driver. Other platforms provide their own equivalents.
 */
val iosAppModule = module {
    single<ObservableSettings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
    single { FocusBloomSettings(get()) }
    single<SqlDriver> { NativeSqliteDriver(focusBloomSyncSchema(), "focusbloom.db") }
    single<FocusBloomDatabase> { createFocusBloomDatabase(get()) }
}
