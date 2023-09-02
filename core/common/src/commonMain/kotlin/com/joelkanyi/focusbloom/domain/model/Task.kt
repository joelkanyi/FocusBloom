package com.joelkanyi.focusbloom.domain.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val color: Long,
    val current: Int,
    val date: LocalDateTime,
)
