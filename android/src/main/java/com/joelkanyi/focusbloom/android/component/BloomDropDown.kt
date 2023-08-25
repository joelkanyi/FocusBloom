package com.joelkanyi.focusbloom.android.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joelkanyi.focusbloom.android.domain.model.TextFieldState
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BloomDropDown(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    placeholder: String,
    options: List<T>,
    enabled: Boolean = true,
    selectedOption: TextFieldState,
    onOptionSelected: (T) -> Unit,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        disabledLabelColor = MaterialTheme.colorScheme.onBackground,
        disabledTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        focusedBorderColor = if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground.copy(alpha = .4f)
        },
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodySmall.copy(
        fontSize = 12.sp,
    ),
    shape: CornerBasedShape = MaterialTheme.shapes.small,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
    ) {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
            )
        }
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = selectedOption.text,
                onValueChange = {},
                shape = shape,
                textStyle = textStyle,
                placeholder = {
                    if (selectedOption.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                            ),
                        )
                    }
                },
                trailingIcon = {
                    if (enabled) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                colors = colors,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectionOption.toString(),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        if (!selectedOption.error.isNullOrEmpty()) {
            Text(
                text = selectedOption.error ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
fun BloomDropDownPreview() {
    FocusBloomTheme {
        BloomDropDown(
            placeholder = "",
            options = listOf("c"),
            selectedOption = TextFieldState("Tree House"),
            onOptionSelected = {},
        )
    }
}
