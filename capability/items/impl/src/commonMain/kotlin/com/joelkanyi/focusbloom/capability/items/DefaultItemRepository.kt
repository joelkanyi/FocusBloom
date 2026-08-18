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

import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.joelkanyi.focusbloom.core.common.DispatcherProvider
import com.joelkanyi.focusbloom.core.common.FocusClock
import com.joelkanyi.focusbloom.core.database.FocusBloomDatabase
import com.joelkanyi.focusbloom.core.model.Item
import com.joelkanyi.focusbloom.core.model.ItemLocation
import com.joelkanyi.focusbloom.core.model.ItemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import com.joelkanyi.focusbloom.core.database.Item as ItemRow

class DefaultItemRepository(
    database: FocusBloomDatabase,
    private val dispatchers: DispatcherProvider,
    private val clock: FocusClock,
) : ItemRepository {

    private val queries = database.itemQueries

    override fun observeByLocation(location: ItemLocation): Flow<List<Item>> =
        queries.selectByLocation(location.name)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows -> rows.map { it.toDomain() } }

    override fun observeAll(): Flow<List<Item>> =
        queries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun byId(id: Long): Item? = withContext(dispatchers.io) {
        queries.selectById(id).awaitAsOneOrNull()?.toDomain()
    }

    override suspend fun upsert(item: Item): Long = withContext(dispatchers.io) {
        if (item.id == 0L) {
            queries.insert(
                type = item.type.name,
                title = item.title,
                note = item.note,
                location = item.location.name,
                drawnOn = item.drawnOn?.toString(),
                createdAt = item.createdAt.toEpochMilliseconds(),
                lastTouchedAt = item.lastTouchedAt.toEpochMilliseconds(),
            )
            queries.lastInsertId().awaitAsOne()
        } else {
            queries.update(
                title = item.title,
                note = item.note,
                location = item.location.name,
                drawnOn = item.drawnOn?.toString(),
                lastTouchedAt = item.lastTouchedAt.toEpochMilliseconds(),
                id = item.id,
            )
            item.id
        }
    }

    override suspend fun move(id: Long, location: ItemLocation, drawnOn: LocalDate?) {
        withContext(dispatchers.io) {
            queries.updateLocation(
                location = location.name,
                drawnOn = drawnOn?.toString(),
                lastTouchedAt = clock.now().toEpochMilliseconds(),
                id = id,
            )
        }
    }

    override suspend fun delete(id: Long) {
        withContext(dispatchers.io) { queries.deleteById(id) }
    }
}

private fun ItemRow.toDomain(): Item = Item(
    id = id,
    type = ItemType.valueOf(type),
    title = title,
    note = note,
    location = ItemLocation.valueOf(location),
    drawnOn = drawnOn?.let(LocalDate::parse),
    createdAt = Instant.fromEpochMilliseconds(createdAt),
    lastTouchedAt = Instant.fromEpochMilliseconds(lastTouchedAt),
)
