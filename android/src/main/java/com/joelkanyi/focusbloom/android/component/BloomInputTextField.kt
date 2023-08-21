package com.joelkanyi.focusbloom.android.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joelkanyi.focusbloom.android.domain.model.TextFieldState

@Composable
fun BloomInputTextField(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    placeholder: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    value: TextFieldState,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        label()
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.text,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
        )
        if (!value.error.isNullOrEmpty()) {
            Text(
                text = value.error,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error,
                ),
            )
        }
    }
}
