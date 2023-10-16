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
    context: CoroutineContext = Dispatchers.Default
): Flow<T> = map {
    withContext(context) {
        it.executeAsOne()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrDefault(
    defaultValue: T,
    context: CoroutineContext = Dispatchers.Default
): Flow<T> = map {
    withContext(context) {
        it.executeAsOneOrNull() ?: defaultValue
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrNull(
    context: CoroutineContext = Dispatchers.Default
): Flow<T?> = map {
    withContext(context) {
        it.executeAsOneOrNull()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneNotNull(
    context: CoroutineContext = Dispatchers.Default
): Flow<T> = mapNotNull {
    withContext(context) {
        it.executeAsOneOrNull()
    }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToList(
    context: CoroutineContext = Dispatchers.Default
): Flow<List<T>> = map {
    withContext(context) {
        it.executeAsList()
    }
}
