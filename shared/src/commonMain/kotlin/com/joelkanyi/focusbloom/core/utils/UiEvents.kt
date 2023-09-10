package com.joelkanyi.focusbloom.core.utils

sealed class UiEvents {
    data class ShowToast(val message: String) : UiEvents()
    data class ShowSnackbar(val message: String) : UiEvents()
    data object NavigateBack : UiEvents()
}
