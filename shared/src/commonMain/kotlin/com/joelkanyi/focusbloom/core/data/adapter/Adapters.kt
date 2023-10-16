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
