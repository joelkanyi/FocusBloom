package com.joelkanyi.focusbloom.android.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.focusbloom.android.domain.model.TextFieldState
import com.joelkanyi.focusbloom.android.ui.theme.FocusBloomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BloomDropDown(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.titleSmall,
    options: List<T>,
    enabled: Boolean = true,
    selectedOption: TextFieldState,
    onOptionSelected: (T) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    shape: CornerBasedShape = MaterialTheme.shapes.small,
) {
    var expanded by remember { mutableStateOf(false) }
    Column() {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
            )
        }
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .border(
                        width = 1.dp,
                        color = if (enabled) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = .4f)
                        },
                        shape = shape,
                    )
                    .clip(shape),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.padding(
                        vertical = 8.dp,
                        horizontal = 12.dp,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = selectedOption.text,
                        style = textStyle,
                    )
                    if (enabled) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                }
            }
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
                modifier = Modifier.fillMaxWidth(),
                text = selectedOption.error,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.End,
            )
        }
    }
}

@Preview
@Composable
fun BloomDropDownPreview() {
    FocusBloomTheme {
        BloomDropDown(
            options = listOf("c"),
            selectedOption = TextFieldState("Tree House"),
            onOptionSelected = {},
        )
    }
}
