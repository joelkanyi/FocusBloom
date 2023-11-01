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
package com.joelkanyi.focusbloom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.joelkanyi.focusbloom.core.presentation.theme.FocusBloomTheme
import com.joelkanyi.focusbloom.core.utils.ProvideAppNavigator
import com.joelkanyi.focusbloom.feature.onboarding.OnboardingScreen
import com.joelkanyi.focusbloom.main.MainScreen
import com.joelkanyi.focusbloom.main.MainViewModel
import com.joelkanyi.focusbloom.main.OnBoardingState
import com.joelkanyi.focusbloom.platform.StatusBarColors
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun FocusBloomApp(
    mainViewModel: MainViewModel = koinInject(),
) {
    val darkTheme = when (mainViewModel.appTheme.collectAsState().value) {
        1 -> true
        else -> false
    }
    val onBoardingCompleted = mainViewModel.onBoardingCompleted.collectAsState().value

    KoinContext {
        FocusBloomTheme(
            useDarkTheme = darkTheme,
        ) {
            StatusBarColors(
                statusBarColor = MaterialTheme.colorScheme.background,
                navBarColor = MaterialTheme.colorScheme.background,
            )
            when (onBoardingCompleted) {
                is OnBoardingState.Success -> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        Navigator(
                            screen = if (onBoardingCompleted.completed) {
                                MainScreen()
                            } else {
                                OnboardingScreen()
                            },
                            content = { navigator ->
                                ProvideAppNavigator(
                                    navigator = navigator,
                                    content = { SlideTransition(navigator = navigator) },
                                )
                            },
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
