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

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

actual class NotificationsManager(
    private val context: Context
) {
    private val notificationManager get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @SuppressLint("MissingPermission")
    actual fun showUpdateNotification(
        title: String,
        description: String
    ) {
        // TODO: use a PendingIntent to open the app on notification click
        // Intent for the notification click
        /*val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            action = UpdateAction
        }*/

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            // TODO: use a PendingIntent to open the app on notification click
            // .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)

        createNotificationChannel()
        if (areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        }
    }

    private fun areNotificationsEnabled() = NotificationManagerCompat
        .from(context)
        .areNotificationsEnabled()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val channelName = "FocusBloom"
        const val channelDescription = "FocusBloom notifications"
        const val notificationId = 42
        const val channelId = "FocusBloom"
    }
}
