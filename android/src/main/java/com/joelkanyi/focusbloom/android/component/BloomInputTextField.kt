package com.joelkanyi.focusbloom.android.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.domain.model.TextFieldState

@Composable
fun BloomInputTextField(
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    value: TextFieldState,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier,
    ) {
        if (label != null) {
            label()
            Spacer(modifier = Modifier.height(4.dp))
        }
        OutlinedTextField(
            value = value.text,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
            shape = shape,
            keyboardOptions = keyboardOptions,
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
