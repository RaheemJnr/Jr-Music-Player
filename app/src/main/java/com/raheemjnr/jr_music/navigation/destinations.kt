import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector


//sealed class AuthScreen(val route: String) {
//    object Login : AuthScreen("login")
//    object ForgetPassword : AuthScreen("forgetPassword")
//    object AuthType : AuthScreen("authType")
//    object UserType : AuthScreen("userType")
//    object UserInfo : AuthScreen("userInfo")
//}

sealed class MainScreen(val route: String?, val title: String?, val icon: ImageVector?) {
    object Local : MainScreen("Local", "Local", Icons.Default.Add)
    object Online : MainScreen("Online", "Online", Icons.Default.Notifications)
//    object Profile : MainScreen("profile", "Profile", Icons.Default.Person)
//    object Camera : MainScreen("camera", null, null)
//    object Admin : MainScreen("admin", null, null)
//    object Contractor : MainScreen("contractor", null, null)

}