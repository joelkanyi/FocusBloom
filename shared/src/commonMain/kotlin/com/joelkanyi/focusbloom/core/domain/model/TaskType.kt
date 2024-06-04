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
import focusbloom.shared.generated.resources.other
import focusbloom.shared.generated.resources.personal
import focusbloom.shared.generated.resources.study
import focusbloom.shared.generated.resources.work
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class TaskType @OptIn(ExperimentalResourceApi::class) constructor(
    val name: String,
    val icon: DrawableResource,
    val color: Long,
) {
    override fun toString(): String {
        return name
    }
}

@OptIn(ExperimentalResourceApi::class)
val taskTypes = listOf(
    TaskType(
        name = "Work",
        icon = Res.drawable.work,
        color = 0xFF3375fd,
    ),
    TaskType(
        name = "Study",
        icon = Res.drawable.study,
        color = 0xFFff686d,
    ),
    TaskType(
        name = "Personal",
        icon = Res.drawable.personal,
        color = 0xFF24c469,
    ),
    TaskType(
        name = "Other",
        icon = Res.drawable.other,
        color = 0xFF734efe,
    ),
)
