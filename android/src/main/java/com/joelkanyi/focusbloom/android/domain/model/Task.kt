package com.joelkanyi.focusbloom.android.domain.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class Task(
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val color: Color,
    val current: Int,
    val date: LocalDateTime,
)
