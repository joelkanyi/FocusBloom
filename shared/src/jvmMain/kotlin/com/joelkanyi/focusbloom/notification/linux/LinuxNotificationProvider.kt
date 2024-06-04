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
package com.joelkanyi.focusbloom.notification.linux

import com.joelkanyi.focusbloom.notification.NotificationProvider
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference

object LinuxNotificationProvider : NotificationProvider {

    private lateinit var libNotify: LibNotify

    override var available: Boolean = false
        private set

    private enum class Error { NONE, LOAD, INIT }

    private var error = Error.NONE

    override val errorMessage: String
        get() = ""

    override fun init() {
        try {
            libNotify = Native.load("libnotify.so.4", LibNotify::class.java) // NON-NLS
        } catch (err: UnsatisfiedLinkError) {
            error = Error.LOAD
            println("unable to load libnotify")
            return
        }

        available = libNotify.notify_init("FocusBloom") // NON-NLS
        if (!available) {
            error = Error.INIT
            println("unable to initialize libnotify")
            return
        }

        // print notification server capabilities
        val list = libNotify.notify_get_server_caps()
        val capabilities = buildList {
            for (i in 0 until libNotify.g_list_length(list)) {
                add(libNotify.g_list_nth_data(list, i).getString(0))
            }
        }
        println("Notification server capabilities: " + capabilities.joinToString())
    }

    override fun uninit() {
        if (available) {
            libNotify.notify_uninit()
            available = false
        }
    }

    override fun sendNotification(title: String, description: String) {
        if (!available) return

        /**
         * summary
         *
         * This is a single line overview of the notification.
         * For instance, "You have mail" or "A friend has come online".
         * It should generally not be longer than 40 characters, though this is not a requirement,
         * and server implementations should word wrap if necessary.
         * The summary must be encoded using UTF-8.
         */
        // todo: we could use body instead with markup (where supported)
        val notification = libNotify.notify_notification_new(title, description, null)

        /**
         * desktop-entry
         *
         * This specifies the name of the desktop filename representing the calling program.
         * This should be the same as the prefix used for the application's .desktop file.
         * An example would be "rhythmbox" from "rhythmbox.desktop".
         * This can be used by the daemon to retrieve the correct icon for the application, for logging purposes, etc.
         */
        // todo: desktop file usually not present for jar file, provide app_icon/image instead?
        libNotify.notify_notification_set_desktop_entry(notification, "com.joelkanyi.focusbloom")

        /**
         * suppress-sound
         *
         * Causes the server to suppress playing any sounds, if it has that ability.
         * This is usually set when the client itself is going to play its own sound.
         */
        libNotify.notify_notification_set_suppress_sound(notification, true)

        /**
         * category
         *
         * The type of notification this is: A received instant message notification.
         */
        libNotify.notify_notification_set_category(notification, "im.received")

        val errorPointer = PointerByReference()
        if (!libNotify.notify_notification_show(notification, errorPointer)) {
            println("error while sending notification via libnotify")
            val error = GErrorStruct(errorPointer.value)
            println("error code: ${error.code}, message: '${error.message}'")
        }
    }

    /**
     * Functions as defined in the source code at
     * https://www.freedesktop.org/software/gstreamer-sdk/data/docs/latest/glib/glib-GVariant.html
     * https://www.freedesktop.org/software/gstreamer-sdk/data/docs/latest/glib/glib-Doubly-Linked-Lists.html
     * https://gitlab.gnome.org/GNOME/libnotify/-/tree/master/libnotify
     */
    @Suppress("FunctionName")
    private interface LibNotify : Library {
        fun g_list_length(list: Pointer): Int
        fun g_list_nth_data(list: Pointer, n: Int): Pointer

        /**
         *  Creates a new boolean GVariant instance -- either TRUE or FALSE.
         *
         *  @param value a gboolean value
         *
         *  @return a floating reference to a new boolean GVariant instance. [transfer none]
         *
         *  @since 2.24
         */
        fun g_variant_new_boolean(value: Boolean): Pointer

        /**
         *  Creates a string GVariant with the contents of [string].
         *
         *  @param string a normal utf8 nul-terminated string
         *
         *  @return a floating reference to a new string GVariant instance. [transfer none]
         *
         *  @since 2.24
         */
        fun g_variant_new_string(string: String): Pointer

        /**
         * Initialize libnotify. This must be called before any other functions.
         *
         * @param app_name The name of the application initializing libnotify.
         *
         * @return true if successful, or false on error.
         */
        fun notify_init(app_name: String): Boolean

        /**
         * Uninitialize libnotify.
         *
         * This should be called when the program no longer needs libnotify for
         * the rest of its lifecycle, typically just before exiting.
         */
        fun notify_uninit()

        /**
         * Synchronously queries the server for its capabilities and returns them in a #GList.
         *
         * @return [Pointer] to a #GList of server capability strings. Free
         *   the list elements with g_free() and the list itself with g_list_free().
         */
        fun notify_get_server_caps(): Pointer

        /**
         * Creates a new #NotifyNotification. The summary text is required, but
         * all other parameters are optional.
         *
         * @param summary The required summary text.
         * @param body The optional body text.
         * @param icon The optional icon theme icon name or filename.
         *
         * @return [Pointer] to the new #NotifyNotification.
         */
        fun notify_notification_new(summary: String, body: String?, icon: String?): Pointer

        /**
         * Tells the notification server to display the notification on the screen.
         *
         * @param notification [Pointer] to the notification.
         * @param error The returned error information.
         *
         * @return true if successful. On error, this will return false and set [error].
         */
        fun notify_notification_show(notification: Pointer, error: PointerByReference?): Boolean

        /**
         * Sets a hint for [key] with value [value]. If [value] is null,
         * a previously set hint for [key] is unset.
         *
         * If [value] is floating, it is consumed.
         *
         * @param notification [Pointer] to a #NotifyNotification
         * @param key the hint key
         * @param value [Pointer] to hint value as GVariant, or null to unset the hint
         *
         * @since 0.6
         */
        fun notify_notification_set_hint(notification: Pointer, key: String, value: Pointer?)

        /**
         * Sets the category of this notification. This can be used by the
         * notification server to filter or display the data in a certain way.
         *
         * @param notification [Pointer] to the notification.
         * @param category The category.
         */
        fun notify_notification_set_category(notification: Pointer, category: String)
    }

    @Suppress("FunctionName")
    private fun LibNotify.notify_notification_set_desktop_entry(
        notification: Pointer,
        desktopEntry: String,
    ) {
        val string = g_variant_new_string(desktopEntry)
        notify_notification_set_hint(notification, "desktop-entry", string) // NON-NLS
    }

    @Suppress("FunctionName")
    private fun LibNotify.notify_notification_set_suppress_sound(
        notification: Pointer,
        suppressSound: Boolean,
    ) {
        val bool = g_variant_new_boolean(suppressSound)
        notify_notification_set_hint(notification, "suppress-sound", bool) // NON-NLS
    }
}
