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
package com.joelkanyi.focusbloom.notification.macos

import com.joelkanyi.focusbloom.notification.NotificationProvider
import de.jangassen.jfa.foundation.Foundation
import de.jangassen.jfa.foundation.Foundation.invoke
import de.jangassen.jfa.foundation.Foundation.nsString

object MacOsNotificationProvider : NotificationProvider {

    override var available: Boolean = true
        private set
    override val errorMessage: String
        get() = "" // currently no situation known where available = false and an error message is required.

    override fun init() {
        // nothing to initialize here
    }

    override fun uninit() {
        removeAllDeliveredNotifications()
    }

    @Suppress("HardCodedStringLiteral")
    override fun sendNotification(title: String, description: String) {
        removeAllDeliveredNotifications()
        val notification = invoke(Foundation.getObjcClass("NSUserNotification"), "new")
        invoke(notification, "setTitle:", nsString(title))
        invoke(notification, "setInformativeText:", nsString(description))
        val center = invoke(Foundation.getObjcClass("NSUserNotificationCenter"), "defaultUserNotificationCenter")
        invoke(center, "deliverNotification:", notification)
    }

    private fun removeAllDeliveredNotifications() {
        val center = invoke(Foundation.getObjcClass("NSUserNotificationCenter"), "defaultUserNotificationCenter")
        invoke(center, "removeAllDeliveredNotifications")
    }
}
