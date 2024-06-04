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
package com.joelkanyi.focusbloom.utils

import com.joelkanyi.focusbloom.utils.ResourceUtils.getResourceAsStream
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

object AudioUtils {

    fun loadAudioFromResource(name: String): Clip? {
        val resourceStream = getResourceAsStream(name) ?: return null
        val bufferedStream =
            BufferedInputStream(resourceStream) // add buffer for mark/reset support
        val audioInputStream = AudioSystem.getAudioInputStream(bufferedStream)
        val sound = AudioSystem.getClip()
        sound.open(audioInputStream)
        return sound
    }

    /**
     * Play audio from the beginning.
     * If it is currently played, it is stopped and restarted.
     */
    fun Clip.play() = this.apply {
        stop()
        flush()
        framePosition = 0
        start()
    }
}
