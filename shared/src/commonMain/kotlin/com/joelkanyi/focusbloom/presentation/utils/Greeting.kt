package com.joelkanyi.focusbloom.presentation.utils

import com.joelkanyi.focusbloom.domain.utils.Platform

class Greeting {
    fun greet(): String {
        return "Hello, ${Platform().platform}!"
    }
}
