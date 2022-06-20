import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector


sealed class MainScreen(val route: String?, val title: String?, val icon: ImageVector?) {
    object Local : MainScreen("Local", "Local", Icons.Default.Add)
    object Gap : MainScreen(null, null, null)
    object Online : MainScreen("Online", "Online", Icons.Default.Notifications)
}