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
package com.joelkanyi.focusbloom.core.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema

// Web uses the async FocusBloomDatabase.Schema with the web-worker driver; the synchronous
// adapter has no meaning here and is never called.
actual fun focusBloomSyncSchema(): SqlSchema<QueryResult.Value<Unit>> =
    error("Web uses the async FocusBloomDatabase.Schema directly")
