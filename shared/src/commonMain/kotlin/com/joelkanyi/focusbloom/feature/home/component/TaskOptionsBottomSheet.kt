/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.core.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskOptionsBottomSheet(
    bottomSheetState: SheetState,
    onClickCancel: (task: Task) -> Unit,
    onClickDelete: (task: Task) -> Unit,
    onClickPushToTomorrow: (task: Task) -> Unit,
    task: Task,
    onDismissRequest: () -> Unit,
    onClickMarkAsCompleted: (task: Task) -> Unit,
    onClickEditTask: (task: Task) -> Unit,
    type: String,
    onClickPushToToday: (task: Task) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Option(
                icon = Icons.Default.Edit,
                text = "Edit Task",
                onClick = {
                    onClickEditTask(task)
                    onDismissRequest()
                },
            )
            Option(
                icon = Icons.Outlined.EditCalendar,
                text = if (type == "overdue") "Push to Today" else "Push to Tomorrow",
                onClick = {
                    if (type == "overdue") onClickPushToToday(task) else onClickPushToTomorrow(task)
                    onDismissRequest()
                },
            )
            Option(
                icon = Icons.Outlined.Done,
                text = "Mark as Completed",
                onClick = {
                    onClickMarkAsCompleted(task)
                    onDismissRequest()
                },
            )
            Option(
                icon = Icons.Outlined.Delete,
                text = "Delete Task",
                onClick = {
                    onClickDelete(task)
                    onDismissRequest()
                },
            )
            Option(
                icon = Icons.Outlined.Close,
                text = "Cancel",
                onClick = {
                    onClickCancel(task)
                    onDismissRequest()
                },
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
