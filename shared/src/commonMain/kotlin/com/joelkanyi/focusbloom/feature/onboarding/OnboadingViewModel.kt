package com.joelkanyi.focusbloom.feature.onboarding

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.joelkanyi.focusbloom.core.domain.repository.settings.SettingsRepository
import com.joelkanyi.focusbloom.core.utils.UiEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboadingViewModel(
    private val settingsRepository: SettingsRepository,
) : ScreenModel {
    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    fun setUsername(username: String) {
        _username.value = username
    }

    fun saveUsername() {
        coroutineScope.launch {
            settingsRepository.saveUsername(username.value.trim())
            _eventsFlow.emit(UiEvents.Navigation)
        }
    }

    val typeWriterTextParts = listOf(
        "be focused",
        "be present",
        "concentrate",
        "be effective",
        "be productive",
        "get things done",
        "be efficient",
        "be organized",
        "be intentional",
        "be disciplined",
        "be motivated",
        "be consistent",
        "be mindful",
    )
}
