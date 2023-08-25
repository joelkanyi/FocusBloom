package com.joelkanyi.focusbloom.android.domain.model

data class Task(
    val id: Int = 0,
    val name: String,
    val duration: Int,
    val current: Int,
    val totalCycles: Int,
)
