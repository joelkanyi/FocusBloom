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
    val current: String,
    val date: LocalDateTime,
    val focusSessions: Int,
    val currentCycle: Int,
    val completed: Boolean,
    val focusTime: Long,
    val shortBreakTime: Long,
    val longBreakTime: Long,
    val consumedFocusTime: Long,
    val consumedShortBreakTime: Long,
    val consumedLongBreakTime: Long,
    val inProgressTask: Boolean,
)
