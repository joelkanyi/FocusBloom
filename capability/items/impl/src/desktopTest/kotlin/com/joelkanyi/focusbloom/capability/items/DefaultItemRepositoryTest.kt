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
package com.joelkanyi.focusbloom.capability.items

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.joelkanyi.focusbloom.core.database.createFocusBloomDatabase
import com.joelkanyi.focusbloom.core.database.focusBloomSyncSchema
import com.joelkanyi.focusbloom.core.model.Item
import com.joelkanyi.focusbloom.core.model.ItemLocation
import com.joelkanyi.focusbloom.core.model.ItemType
import com.joelkanyi.focusbloom.core.testing.FakeFocusClock
import com.joelkanyi.focusbloom.core.testing.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultItemRepositoryTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun repository(): DefaultItemRepository {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY, schema = focusBloomSyncSchema())
        return DefaultItemRepository(
            database = createFocusBloomDatabase(driver),
            dispatchers = TestDispatcherProvider(UnconfinedTestDispatcher()),
            clock = FakeFocusClock(Instant.fromEpochMilliseconds(5_000)),
        )
    }

    @Test
    fun upsertInsertsAndReadsBack() = runTest {
        val repo = repository()
        val now = Instant.fromEpochMilliseconds(1_000)
        val id = repo.upsert(
            Item(
                id = 0,
                type = ItemType.TASK,
                title = "Write the PR",
                location = ItemLocation.SHELF,
                createdAt = now,
                lastTouchedAt = now,
            ),
        )

        val loaded = repo.byId(id)
        assertEquals("Write the PR", loaded?.title)
        assertEquals(ItemType.TASK, loaded?.type)
        assertEquals(ItemLocation.SHELF, loaded?.location)
    }

    @Test
    fun moveChangesLocationAndDelete() = runTest {
        val repo = repository()
        val now = Instant.fromEpochMilliseconds(1_000)
        val id = repo.upsert(
            Item(id = 0, type = ItemType.TASK, title = "Draw me", createdAt = now, lastTouchedAt = now),
        )

        repo.move(id, ItemLocation.TODAY, drawnOn = null)
        assertEquals(ItemLocation.TODAY, repo.byId(id)?.location)

        repo.delete(id)
        assertNull(repo.byId(id))
    }
}
