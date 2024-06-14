package viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginInfo(val uid: String? = null, val name: String? = null, val header: String? = null)

class LoginViewModel private constructor() : ViewModel() {

    companion object {
        val Instance: LoginViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LoginViewModel() }
    }

    private var loginInfo = MutableStateFlow<LoginInfo>(LoginInfo())
    val loginInfoFlow = loginInfo.asStateFlow()

    fun updateLoginInfo(info: LoginInfo) {
        loginInfo.value = info
    }


}