package com.joelkanyi.focusbloom.presentation.viewmodels

import com.joelkanyi.focusbloom.presentation.utils.Greeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MainViewModel constructor(private val greetingRepository: Greeting) : KoinComponent {

    // @NativeCoroutineScope
    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    private val _greeting = MutableStateFlow<String?>(null)
    val greeting get() = _greeting

    init {
        greetings()
    }

    private fun greetings() = viewModelScope.launch {
        _greeting.value = greetingRepository.greet()
    }
}
