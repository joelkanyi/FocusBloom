package com.joelkanyi.focusbloom.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val type: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val color: Long,
    val current: Int,
    val date: LocalDateTime,
    val focusSessions: Int,
    val completed: Boolean,
    val focusTime: Int,
    val shortBreakTime: Int,
    val longBreakTime: Int,
)