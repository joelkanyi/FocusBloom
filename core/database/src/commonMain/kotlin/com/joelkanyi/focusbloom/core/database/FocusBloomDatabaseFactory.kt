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
package com.joelkanyi.focusbloom.core.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver

/**
 * Builds the database from a platform [SqlDriver]. Each app creates the driver
 * (the Web driver needs a web worker webpack cannot supply from here) and its
 * schema, then hands it in. Repositories in :capability:* impls take the result.
 */
fun createFocusBloomDatabase(driver: SqlDriver): FocusBloomDatabase =
    FocusBloomDatabase(
        driver = driver,
        focusSessionAdapter = FocusSession.Adapter(
            focusMinutesAdapter = IntColumnAdapter,
            shortBreakMinutesAdapter = IntColumnAdapter,
            longBreakMinutesAdapter = IntColumnAdapter,
            cyclesAdapter = IntColumnAdapter,
        ),
    )
