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

import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Structure.FieldOrder

@FieldOrder("domain", "code", "message") // NON-NLS
class GErrorStruct : Structure {
    @JvmField
    @Volatile
    var domain = 0

    @JvmField
    @Volatile
    var code = 0

    @JvmField
    @Volatile
    var message: String? = null

    constructor() {
        clear()
    }

    constructor(ptr: Pointer?) : super(ptr) {
        read()
    }
}
