package com.joelkanyi.focusbloom.core.domain.model

data class TaskType(
    val name: String,
    val icon: String,
    val color: Long,
) {
    override fun toString(): String {
        return name
    }
}

val taskTypes = listOf(
    TaskType(
        name = "Work",
        icon = "work.xml",
        color = 0xFF3375fd,
    ),
    TaskType(
        name = "Study",
        icon = "study.xml",
        color = 0xFFff686d,
    ),
    TaskType(
        name = "Personal",
        icon = "personal.xml",
        color = 0xFF24c469,
    ),
    TaskType(
        name = "Other",
        icon = "other.xml",
        color = 0xFF734efe,
    ),
)
