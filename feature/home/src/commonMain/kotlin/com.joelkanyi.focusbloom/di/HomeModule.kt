package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.Home
import com.joelkanyi.focusbloom.presentation.HomeViewModel
import org.koin.dsl.module

val homeModule = module {
    single { Home() }
    single { HomeViewModel(home = get()) }
}
