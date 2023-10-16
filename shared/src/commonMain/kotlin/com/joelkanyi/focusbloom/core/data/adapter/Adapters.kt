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
package com.joelkanyi.focusbloom.core.data.adapter

import app.cash.sqldelight.ColumnAdapter

val idAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}

val activeAdapter = object : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean {
        return databaseValue.toInt() == 1
    }

    override fun encode(value: Boolean): Long {
        return if (value) 1 else 0
    }
}

val completedAdapter = object : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean {
        return databaseValue.toInt() == 1
    }

    override fun encode(value: Boolean): Long {
        return if (value) 1 else 0
    }
}

val colorAdapter = object : ColumnAdapter<Long, Long> {
    override fun decode(databaseValue: Long): Long {
        return databaseValue
    }

    override fun encode(value: Long): Long {
        return value
    }
}

val consumedFocusTimeAdapter = object : ColumnAdapter<Long, Long> {
    override fun decode(databaseValue: Long): Long {
        return databaseValue
    }

    override fun encode(value: Long): Long {
        return value
    }
}

val consumedLongBreakTimeAdapter = object : ColumnAdapter<Long, Long> {
    override fun decode(databaseValue: Long): Long {
        return databaseValue
    }

    override fun encode(value: Long): Long {
        return value
    }
}

val consumedShortBreakTimeAdapter = object : ColumnAdapter<Long, Long> {
    override fun decode(databaseValue: Long): Long {
        return databaseValue
    }

    override fun encode(value: Long): Long {
        return value
    }
}

val currentAdapter = object : ColumnAdapter<String, String> {
    override fun decode(databaseValue: String): String {
        return databaseValue
    }

    override fun encode(value: String): String {
        return value
    }
}

val currentCycleAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}

val focusSessionsAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}

val inProgressTaskAdapter = object : ColumnAdapter<Boolean, Long> {
    override fun decode(databaseValue: Long): Boolean {
        return databaseValue.toInt() == 1
    }

    override fun encode(value: Boolean): Long {
        return if (value) 1 else 0
    }
}
