/*
 * Copyright 2023 Joel Kanyi.
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
package com.joelkanyi.focusbloom.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.joelkanyi.focusbloom.database.BloomDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            url = "jdbc:sqlite:${databaseFile.absolutePath}",
        ).also { db ->
            BloomDatabase.Schema.create(db)
        }
    }

    private val databaseFile: File
        get() = File(appDir.also { if (!it.exists()) it.mkdirs() }, "bloom.db")

    private val appDir: File
        get() {
            val os = System.getProperty("os.name").lowercase()
            return when {
                os.contains("win") -> {
                    File(
                        System.getenv("AppData"),
                        "bloom/db"
                    ) // "C:\Users<username>\AppData\Roaming\bloom\db"
                }

                os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                    File(
                        System.getProperty("user.home"), ".bloom"
                    ) // "/home/<username>/.bloom"
                }

                os.contains("mac") -> {
                    File(
                        System.getProperty("user.home"),
                        "Library/Application Support/bloom"
                    ) // "/Users/<username>/Library/Application Support/bloom"
                }

                else -> error("Unsupported operating system")
            }
        }
}
