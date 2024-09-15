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
package com.joelkanyi.focusbloom.core.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.joelkanyi.focusbloom.core.presentation.navigation.Destinations
import com.joelkanyi.focusbloom.core.presentation.navigation.NavRail
import org.jetbrains.compose.resources.painterResource

@Composable
fun BloomNavigationRailBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight().alpha(0.95F),
        containerColor = MaterialTheme.colorScheme.surface,
        header = {},
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("?")
            ?: Destinations.Onboarding::class.qualifiedName.orEmpty()

        NavRail.entries.forEachIndexed { index, navigationItem ->
            val isSelected by remember(currentRoute) {
                derivedStateOf { currentRoute == navigationItem.route::class.qualifiedName }
            }
            if (index == NavRail.entries.size - 1) {
                Spacer(Modifier.weight(1f))
            }
            NavigationRailItem(
                modifier = Modifier.padding(vertical = 12.dp),
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon),
                        contentDescription = navigationItem.label,
                    )
                },
                label = {
                    Text(
                        text = navigationItem.label,
                    )
                },
                alwaysShowLabel = true,
                selected = isSelected,
                onClick = {
                    navController.navigate(navigationItem.route)
                },
            )
        }
    }
}
