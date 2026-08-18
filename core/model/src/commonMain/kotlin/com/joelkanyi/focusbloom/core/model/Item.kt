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
package com.joelkanyi.focusbloom.core.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

/** What kind of thing an [Item] is. New kinds are added here, not as new top-level types. */
enum class ItemType { TASK, NOTE, SESSION, EVENT, GOAL, DOC }

/** Where an [Item] sits in the day: the full pool, drawn into today, or resting in the compost. */
enum class ItemLocation { SHELF, TODAY, COMPOST }

/**
 * The single content record. Every task, note, session, event, goal, and document is an [Item],
 * so the app can grow in capability without a new top-level type per feature. Type-specific data
 * lives in dedicated value types (for example [SessionPreset]), never as dozens of optional
 * fields here; that discipline is what keeps [Item] domain-meaningful rather than a generic
 * database row.
 */
data class Item(
    val id: Long,
    val type: ItemType,
    val title: String,
    val note: String? = null,
    val location: ItemLocation = ItemLocation.SHELF,
    val drawnOn: LocalDate? = null,
    val createdAt: Instant,
    val lastTouchedAt: Instant,
)
