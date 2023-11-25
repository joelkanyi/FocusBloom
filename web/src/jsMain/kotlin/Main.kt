
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.joelkanyi.focusbloom.FocusBloomApp
import com.joelkanyi.focusbloom.di.KoinInit
import org.jetbrains.skiko.wasm.onWasmReady

// private val koin = KoinInit().init()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    KoinInit().init()
    onWasmReady {
        CanvasBasedWindow("Focus Bloom") {
            FocusBloomApp()
        }
    }
}
