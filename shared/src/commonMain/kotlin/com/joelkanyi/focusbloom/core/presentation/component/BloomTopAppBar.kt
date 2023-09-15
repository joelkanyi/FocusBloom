package com.joelkanyi.focusbloom.core.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloomTopAppBar(
    modifier: Modifier = Modifier,
    hasBackNavigation: Boolean = false,
    actions: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
    ),
    title: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        actions = {
            if (actions != null) {
                actions()
            }
        },
        navigationIcon = {
            if (hasBackNavigation) {
                if (navigationIcon != null) {
                    navigationIcon()
                }
            }
        },
        colors = colors,
    )
}
