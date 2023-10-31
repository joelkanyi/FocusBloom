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
package com.joelkanyi.focusbloom.core.data.utils

import app.cash.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmOverloads

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOne(
    context: CoroutineContext = Dispatchers.Default,
): Flow<T> = map {
    withContext(context) {
        it.executeAsOne()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrDefault(
    defaultValue: T,
    context: CoroutineContext = Dispatchers.Default,
): Flow<T> = map {
    withContext(context) {
        it.executeAsOneOrNull() ?: defaultValue
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrNull(
    context: CoroutineContext = Dispatchers.Default,
): Flow<T?> = map {
    withContext(context) {
        it.executeAsOneOrNull()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneNotNull(
    context: CoroutineContext = Dispatchers.Default,
): Flow<T> = mapNotNull {
    withContext(context) {
        it.executeAsOneOrNull()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToList(
    context: CoroutineContext = Dispatchers.Default,
): Flow<List<T>> = map {
    withContext(context) {
        it.executeAsList()
    }
}
