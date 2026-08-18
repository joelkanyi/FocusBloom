package com.joelkanyi.focusbloom.shell

import com.joelkanyi.focusbloom.capability.items.DefaultItemRepository
import com.joelkanyi.focusbloom.capability.items.ItemRepository
import com.joelkanyi.focusbloom.capability.session.DefaultSessionController
import com.joelkanyi.focusbloom.capability.session.SessionController
import com.joelkanyi.focusbloom.core.common.DefaultDispatcherProvider
import com.joelkanyi.focusbloom.core.common.DispatcherProvider
import com.joelkanyi.focusbloom.core.common.FocusClock
import com.joelkanyi.focusbloom.core.common.SystemFocusClock
import com.joelkanyi.focusbloom.feature.focus.FocusViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * The cross-platform composition graph. Every launcher includes this and adds only its platform
 * pieces (the settings store and the SQL driver). This binds capability impls to their apis and
 * the feature view models.
 */
val appModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<FocusClock> { SystemFocusClock }
    single<SessionController> { DefaultSessionController(get()) }
    single<ItemRepository> { DefaultItemRepository(get(), get(), get()) }
    viewModelOf(::FocusViewModel)
}
