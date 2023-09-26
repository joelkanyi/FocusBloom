package com.joelkanyi.focusbloom.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Cancel
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
import com.joelkanyi.focusbloom.feature.home.Option

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskOptionsBottomSheet(
    bottomSheetState: SheetState,
    onClickCancel: (task: Task) -> Unit,
    onClickSave: (task: Task) -> Unit,
    onClickDelete: (task: Task) -> Unit,
    onClickPushToTomorrow: (task: Task) -> Unit,
    task: Task,
    onDismissRequest: () -> Unit,
    onClickMarkAsCompleted: (task: Task) -> Unit,
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
                    onDismissRequest()
                },
            )
            Option(
                icon = Icons.Outlined.EditCalendar,
                text = "Push to Tomorrow",
                onClick = {
                    onClickPushToTomorrow(task)
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
