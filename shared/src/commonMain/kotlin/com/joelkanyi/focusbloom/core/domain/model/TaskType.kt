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
package com.joelkanyi.focusbloom.core.domain.model

import focusbloom.shared.generated.resources.Res
import focusbloom.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

data class TaskType @OptIn(ExperimentalResourceApi::class) constructor(
    val name: StringResource,
    val displayName: String,
    val icon: DrawableResource,
    val color: Long,
) {
    override fun toString(): String {
        return displayName
    }
}

@OptIn(ExperimentalResourceApi::class)
val taskTypes = listOf(
    TaskType(
        name = Res.string.task_type_work,
        displayName = "Work",
        icon = Res.drawable.work,
        color = 0xFF3375fd,
    ),
    TaskType(
        name = Res.string.task_type_study,
        displayName = "Study",
        icon = Res.drawable.study,
        color = 0xFFff686d,
    ),
    TaskType(
        name = Res.string.task_type_personal,
        displayName = "Personal",
        icon = Res.drawable.personal,
        color = 0xFF24c469,
    ),
    TaskType(
        name = Res.string.task_type_other,
        displayName = "Other",
        icon = Res.drawable.other,
        color = 0xFF734efe,
    ),
)
