package com.joelkanyi.focusbloom.core.domain.model

internal sealed class Theme(val value: Int) {
    data object Light : Theme(0)
    data object Dark : Theme(1)
    data object System : Theme(2)
}
