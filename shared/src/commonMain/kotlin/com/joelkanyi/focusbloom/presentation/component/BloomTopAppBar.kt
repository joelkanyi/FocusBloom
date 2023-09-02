package com.joelkanyi.focusbloom.presentation.component

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
    actions: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
    ),
    title: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = title,
        actions = {
            actions()
        },
        navigationIcon = {
            if (hasBackNavigation) {
                navigationIcon()
            }
        },
        colors = colors,
    )
}
