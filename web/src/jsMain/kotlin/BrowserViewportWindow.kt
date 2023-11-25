@file:Suppress(
    "INVISIBLE_MEMBER",
    "INVISIBLE_REFERENCE",
    "EXPOSED_PARAMETER_TYPE",
)

/*
 * Copyright 2022 The Android Open Source Project
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.createSkiaLayer
import androidx.compose.ui.native.ComposeLayer
import androidx.compose.ui.platform.JSTextInputService
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.ComposeWindow
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.HTMLTitleElement

/**
 * A Compose Web Js/Skiko/Canvas-based top-level window using the browser's entire viewport. Supports resizing.
 */
@Suppress("FunctionName")
fun BrowserViewportWindow(
    title: String = "Untitled",
    content: @Composable () -> Unit,
) {
    AutoSizingComposeWindow(title).apply {
        setContent {
            content()
        }
    }
}

private const val CANVAS_ELEMENT_ID = "ComposeTarget"

private class AutoSizingComposeWindow(title: String) {

    private val density: Density = Density(
        density = window.devicePixelRatio.toFloat(),
        fontScale = 1f,
    )

    private val jsTextInputService = JSTextInputService()
    val platform = ComposeWindow().platform
    private val layer = ComposeLayer(
        layer = createSkiaLayer(),
        platform = platform,
        // getTopLeftOffset = { Offset.Zero },
        input = jsTextInputService.input,
    )

    // TODO: generalize me.
    val canvas = document.getElementById(CANVAS_ELEMENT_ID) as HTMLCanvasElement

    private val htmlHeadElement = document.head!!

    private fun HTMLCanvasElement.fillViewportSize() {
        setAttribute("width", "${window.innerWidth}")
        setAttribute("height", "${window.innerHeight}")
    }

    init {
        htmlHeadElement.appendChild(
            (document.createElement("style") as HTMLStyleElement).apply {
                type = "text/css"
                appendChild(
                    document.createTextNode(
                        """
                        html, body {
                            overflow: hidden;
                            margin: 0 !important;
                            padding: 0 !important;
                        }
    
                        #$CANVAS_ELEMENT_ID {
                            outline: none;
                        }
                        """.trimIndent(),
                    ),
                )
            },
        )

        canvas.fillViewportSize()

        layer.layer.attachTo(canvas)
        canvas.setAttribute("tabindex", "0")

        layer.setSize(canvas.width, canvas.height)

        window.addEventListener("resize", {
            val scale = layer.layer.contentScale
            val density = window.devicePixelRatio.toFloat()
            canvas.fillViewportSize()
            layer.layer.attachTo(canvas)
            layer.layer.needRedraw()
            layer.setSize(
                (canvas.width / scale * density).toInt(),
                (canvas.height / scale * density).toInt(),
            )
        })

        val htmlTitleElement = (
            htmlHeadElement.getElementsByTagName("title").item(0)
                ?: document.createElement("title").also { htmlHeadElement.appendChild(it) }
            ) as HTMLTitleElement
        htmlTitleElement.textContent = title
    }

    /**
     * Sets Compose content of the ComposeWindow.
     *
     * @param content Composable content of the ComposeWindow.
     */
    fun setContent(
        content: @Composable () -> Unit,
    ) {
        layer.setDensity(density)
        layer.setContent(
            content = content,
        )
    }

    // TODO: need to call .dispose() on window close.
    fun dispose() {
        layer.dispose()
    }
}
