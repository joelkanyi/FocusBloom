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
package com.joelkanyi.focusbloom.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.joelkanyi.focusbloom.core.utils.UiEvents
import com.joelkanyi.focusbloom.main.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.compose.rememberKoinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class UsernameScreen : Screen, KoinComponent {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val onboadingViewModel = rememberKoinInject<OnboadingViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val username = onboadingViewModel.username.collectAsState().value
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(Unit) {
            withContext(Dispatchers.Main.immediate) {
                onboadingViewModel.eventsFlow.collect { event ->
                    when (event) {
                        is UiEvents.Navigation -> {
                            navigator.replaceAll(MainScreen())
                        }

                        else -> {}
                    }
                }
            }
        }
        UsernameScreenContent(
            username = username,
            typeWriterTextParts = onboadingViewModel.typeWriterTextParts,
            onUsernameChange = {
                onboadingViewModel.setUsername(it)
            },
            onClickContinue = {
                keyboardController?.hide()
                onboadingViewModel.saveUsername()
            },
        )
    }
}

@Composable
fun UsernameScreenContent(
    username: String,
    typeWriterTextParts: List<String>,
    onUsernameChange: (String) -> Unit,
    onClickContinue: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        item {
            TypewriterText(
                baseText = "Focus Bloom app is what you need to",
                parts = typeWriterTextParts,
            )
        }

        item {
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "What's your username?",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 18.sp,
                ),
            )
        }

        item {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            UsernameTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                name = username,
                onNameChange = {
                    onUsernameChange(it)
                },
                onClickDone = {
                    onClickContinue()
                },
            )
        }

        item {
            AnimatedVisibility(
                username.isNotEmpty(),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(56.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = MaterialTheme.shapes.medium,
                        onClick = onClickContinue,
                    ) {
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UsernameTextField(
    modifier: Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    onClickDone: () -> Unit,
) {
    TextField(
        modifier = modifier,
        value = name,
        onValueChange = onNameChange,
        maxLines = 1,
        singleLine = true,
        placeholder = {
            Text(
                text = "John Doe",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 18.sp,
                    letterSpacing = -(1.6).sp,
                    lineHeight = 32.sp,
                ),
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
            disabledIndicatorColor = MaterialTheme.colorScheme.primary,
        ),
        textStyle = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onClickDone()
            },
        ),
    )
}

@Composable
private fun TypewriterText(modifier: Modifier = Modifier, baseText: String, parts: List<String>) {
    var partIndex by remember { mutableStateOf(0) }
    var partText by remember { mutableStateOf("") }
    val textToDisplay = "$baseText $partText"
    Text(
        modifier = modifier,
        text = textToDisplay,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            letterSpacing = -(1.6).sp,
            lineHeight = 32.sp,
        ),
    )

    LaunchedEffect(key1 = parts) {
        while (partIndex <= parts.size) {
            val part = parts[partIndex]

            part.forEachIndexed { charIndex, _ ->
                partText = part.substring(startIndex = 0, endIndex = charIndex + 1)
                delay(100)
            }

            delay(1500)

            part.forEachIndexed { charIndex, _ ->
                partText = part
                    .substring(startIndex = 0, endIndex = part.length - (charIndex + 1))
                delay(30)
            }

            delay(500)

            partIndex = (partIndex + 1) % parts.size
        }
    }
}
