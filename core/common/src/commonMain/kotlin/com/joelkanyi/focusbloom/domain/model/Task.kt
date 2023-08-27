package com.joelkanyi.focusbloom.domain.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int = 0,
    val description: String? = null,
    val name: String,
    val current: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val date: LocalDateTime,
    val color: Int,
)
