package com.joelkanyi.focusbloom.core.data.local.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

class DateTimeAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime = databaseValue.toLocalDateTime()
    override fun encode(value: LocalDateTime): String = value.toString()
}
