import androidx.compose.ui.window.ComposeUIViewController
import com.ctrip.flight.mmkv.initialize

fun MainViewController() = ComposeUIViewController {
    initialize()
    App()
}