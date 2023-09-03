package com.joelkanyi.focusbloom.presentation.utils

import com.joelkanyi.focusbloom.Platform

class Greeting {
    fun greet(): String {
        return "Hello, ${Platform().platform}!"
    }
}
