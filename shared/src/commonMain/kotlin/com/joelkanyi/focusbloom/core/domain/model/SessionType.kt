package com.joelkanyi.focusbloom.core.domain.model

sealed class SessionType {
    data object Focus : SessionType()
    data object ShortBreak : SessionType()
    data object LongBreak : SessionType()
}
