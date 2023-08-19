import androidx.compose.ui.window.application
import com.joelkanyi.focusbloom.di.calendarModule
import com.joelkanyi.focusbloom.di.homeModule
import com.joelkanyi.focusbloom.di.initKoin
import com.joelkanyi.focusbloom.di.settingsModule
import com.joelkanyi.focusbloom.di.statisticsModule
import org.koin.core.Koin
import ui.screens.MainScreen

lateinit var koin: Koin

fun main() {
    koin = initKoin().koin
    koin.loadModules(
        listOf(
            homeModule,
            settingsModule,
            calendarModule,
            statisticsModule,
        ),
    )

    return application {
        MainScreen(applicationScope = this)
    }
}
