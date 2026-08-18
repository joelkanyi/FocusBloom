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

import com.joelkanyi.focusbloom.core.model.Item
import com.joelkanyi.focusbloom.core.model.ItemLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * The store for [Item]s. Features reach the database only through this interface (the impl lives
 * in :capability:items:impl and is known only to the composition roots). Reads are observable
 * flows; writes are suspend functions.
 */
interface ItemRepository {
    fun observeByLocation(location: ItemLocation): Flow<List<Item>>

    fun observeAll(): Flow<List<Item>>

    suspend fun byId(id: Long): Item?

    /** Inserts (when id == 0) or updates an existing item; returns the item's id. */
    suspend fun upsert(item: Item): Long

    /** Moves an item between shelf, today, and compost, stamping lastTouchedAt. */
    suspend fun move(id: Long, location: ItemLocation, drawnOn: LocalDate?)

    suspend fun delete(id: Long)
}
