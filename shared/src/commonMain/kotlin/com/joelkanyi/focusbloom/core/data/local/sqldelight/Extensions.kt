package com.joelkanyi.focusbloom.core.data.local.sqldelight

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.coroutines.asFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any, R : Any?> Flow<Query<T>>.flatMapLatestAs(map: suspend (Query<T>) -> R): Flow<R> =
    flatMapLatest { query ->
        query.asFlow().map(map)
    }

fun <T : Any, R : Any?> Flow<Query<T>>.flatMapLatestAsOne(map: suspend (T) -> R): Flow<R> =
    flatMapLatestAs(Query<T>::awaitAsOne).map(map)

fun <T : Any, R : Any?> Flow<Query<T>>.flatMapLatestAsOneOrNull(map: suspend (T?) -> R): Flow<R> =
    flatMapLatestAs(Query<T>::awaitAsOneOrNull).map(map)

fun <T : Any, R : Any?> Flow<Query<T>>.flatMapLatestAsList(map: suspend (List<T>) -> R): Flow<R> =
    flatMapLatestAs(Query<T>::awaitAsList).map(map)

fun <T : Any> Flow<Query<T>>.flatMapLatestAsOne(): Flow<T> = flatMapLatestAs(Query<T>::awaitAsOne)
fun <T : Any> Flow<Query<T>>.flatMapLatestAsOneOrNull(): Flow<T?> =
    flatMapLatestAs(Query<T>::awaitAsOneOrNull)

fun <T : Any> Flow<Query<T>>.flatMapLatestAsList(): Flow<List<T>> =
    flatMapLatestAs(Query<T>::awaitAsList)
