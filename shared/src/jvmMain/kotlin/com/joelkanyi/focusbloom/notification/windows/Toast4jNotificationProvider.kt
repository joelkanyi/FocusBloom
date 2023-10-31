/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.focusbloom.notification.windows

import com.joelkanyi.focusbloom.notification.NotificationProvider
import de.mobanisto.toast4j.ToastBuilder
import de.mobanisto.toast4j.ToastHandle
import de.mobanisto.toast4j.Toaster
import de.mobanisto.wintoast.WinToastTemplate

object Toast4jNotificationProvider : NotificationProvider {

    private lateinit var toaster: Toaster

    override var available: Boolean = false
        private set

    private enum class Error { NONE, INIT }

    private var error = Error.NONE

    override val errorMessage: String
        get() = ""

    override fun init() {
        toaster = Toaster.forAumi("com.joelkanyi.focusbloom")
        available = toaster.initialize()
        if (!available) {
            error = Error.INIT
            println("unable to initialize toast4j")
            return
        }
    }

    override fun uninit() {
        currentToast?.hide()
    }

    private var currentToast: ToastHandle? = null

    override fun sendNotification(title: String, description: String) {
        currentToast?.hide()
        currentToast = toaster.showToast(
            ToastBuilder(WinToastTemplate.WinToastTemplateType.ToastText01).setSilent()
                .setLine1(title)
                .setLine2(description)
                .build(),
        )
    }
}
