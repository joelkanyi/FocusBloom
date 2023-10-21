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
package com.joelkanyi.focusbloom.platform

import platform.Foundation.NSUUID
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

actual class NotificationsManager {
    actual fun showUpdateNotification(title: String, description: String) {
        val content = UNMutableNotificationContent()
        content.setTitle(title)
        content.setBody(description)
        content.setSound(UNNotificationSound.defaultSound())

        val uuid = NSUUID.UUID().UUIDString()
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content, null)

        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) {
                withCompletionHandler()
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                withCompletionHandler(UNNotificationPresentationOptionAlert)
            }
        }

        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error: $error")
            } else {
                println("Notification sent")
            }
        }
    }
}
