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

/** The small, fixed set of ways two items relate. Kept closed so the model stays meaningful. */
enum class RelationKind { BELONGS_TO, REFERENCES, BLOCKS }

/**
 * A typed, directional link between two items: a task belongs to a project, a session references a
 * task, one task blocks another. Relations are how structure emerges without a generic graph.
 */
data class Relation(
    val fromId: Long,
    val toId: Long,
    val kind: RelationKind,
)
