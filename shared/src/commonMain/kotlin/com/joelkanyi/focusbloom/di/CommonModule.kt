package com.joelkanyi.focusbloom.di

import com.joelkanyi.focusbloom.presentation.utils.Greeting
import com.joelkanyi.focusbloom.presentation.viewmodels.MainViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(isDebug: Boolean) = module {
    singleOf(::Greeting)

    singleOf(::MainViewModel)
}

// expect fun platformModule(): Module
