package com.joelkanyi.focusbloom.presentation

import com.joelkanyi.focusbloom.Home
import org.koin.core.component.KoinComponent

class HomeViewModel(private val home: Home) : KoinComponent {

    fun getHome(): String {
        return home.getHome()
    }
}
