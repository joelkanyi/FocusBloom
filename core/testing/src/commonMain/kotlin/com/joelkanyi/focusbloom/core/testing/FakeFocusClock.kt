/*
 * Copyright 2026 Joel Kanyi.
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
package com.joelkanyi.focusbloom.core.testing

import com.joelkanyi.focusbloom.core.common.FocusClock
import kotlinx.datetime.Instant

/** A [FocusClock] whose "now" is fixed and settable, for deterministic time in tests. */
class FakeFocusClock(
    var instant: Instant = Instant.fromEpochMilliseconds(0),
) : FocusClock {
    override fun now(): Instant = instant
}
