package com.joelkanyi.focusbloom.samples

import androidx.compose.ui.graphics.Color
import com.joelkanyi.focusbloom.android.domain.model.Task
import java.time.LocalDateTime

val sampleTasks = listOf(
    // Today
    Task(
        name = "iOS App Development with Swift",
        color = Color(0xFFF4BFDB),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-08-27T10:00:00"),
        end = LocalDateTime.parse("2021-08-27T11:00:00"),
        description = "Learn how to build iOS apps with Swift.",
    ),
    Task(
        name = "Implement Desktop support",
        color = Color(0xFF6DD3CE),
        date = LocalDateTime.parse("2021-05-18T01:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-08-27T11:00:00"),
        end = LocalDateTime.parse("2021-08-27T11:45:00"),
        description = "Implement Desktop support for the app.",
    ),
    // Tomorrow
    Task(
        name = "Implement the new feature",
        color = Color(0xFF1B998B),
        date = LocalDateTime.parse("2021-05-18T20:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-08-28T20:00:00"),
        end = LocalDateTime.parse("2021-08-28T22:00:00"),
        description = "Clean up the code and implement the new feature.",
    ),
    Task(
        name = "Refactor the code to make it more readable",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T08:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-08-28T08:00:00"),
        end = LocalDateTime.parse("2021-08-28T10:00:00"),
        description = "Write some tests and refactor the code to make it more readable.",
    ),
    // The day after tomorrow
    Task(
        name = "Write a blog post about the new feature",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-08-29T09:00:00"),
        end = LocalDateTime.parse("2021-08-29T11:00:00"),
        description = "I need to write a blog post about the new feature.",
    ),
/*    Task(
        name = "Add Cocoapods to the common module for iOS",
        color = Color(0xFFAFBBF2),
        current = 4,
        date = LocalDateTime.parse("2021-05-18T00:00:00"),
        start = LocalDateTime.parse("2021-05-18T00:00:00"),
        end = LocalDateTime.parse("2021-05-18T01:00:00"),
        description = "I need to add Cocoapods to the common module for iOS, regular framework won't work.",
    ),*/
/*    Task(
        name = "Research about the best way to implement a new feature",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T02:00:00"),
        end = LocalDateTime.parse("2021-05-18T04:00:00"),
        description = "Take a look at the current implementation and see if there's a better way to do it.",
    ),*/
    Task(
        name = "Ride my bike around the city",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T06:00:00"),
        end = LocalDateTime.parse("2021-05-18T07:00:00"),
        description = "I need to ride my bike around the city to get some fresh air.",
    ),
    Task(
        name = "Write a blog post about the new feature",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T09:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "I need to write a blog post about the new feature.",
    ),
    Task(
        name = "Refactor the code to make it more readable",
        color = Color(0xFFAFBBF2),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T09:00:00"),
        end = LocalDateTime.parse("2021-05-18T10:00:00"),
        description = "Write some tests and refactor the code to make it more readable.",
    ),
    Task(
        name = "Implement the new feature",
        color = Color(0xFF1B998B),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T10:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "Clean up the code and implement the new feature.",
    ),
    Task(
        name = "Implement Desktop support",
        color = Color(0xFF6DD3CE),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T11:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:45:00"),
        description = "Implement Desktop support for the app.",
    ),
    Task(
        name = "iOS App Development with Swift",
        color = Color(0xFFF4BFDB),
        date = LocalDateTime.parse("2021-05-18T12:00:00"),
        current = 4,
        start = LocalDateTime.parse("2021-05-18T10:00:00"),
        end = LocalDateTime.parse("2021-05-18T11:00:00"),
        description = "Learn how to build iOS apps with Swift.",
    ),
)
