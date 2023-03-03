//package ter.den.movie.presentation
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import ter.den.config.AppConfig
//import ter.den.feature_auth.data.AuthDataSource
//import javax.inject.Inject
//
//class MainViewModel @Inject constructor(
//    private val appConfig: AppConfig,
//    private val authDataSource: AuthDataSource,
//) : ViewModel() {
//
//    init {
//        viewModelScope.launch {
//            var sessionId = appConfig.sessionId.stateIn(viewModelScope).value
//            if (sessionId.isNullOrEmpty()) {
//                val newSessionId = authDataSource.createSessionId(
//                    "denter5858",
//                    "den2003"
//                )
//                Log.e("DDDD", "CREATED "+ newSessionId)
//                sessionId = "fsfs"
//            }
//            Log.e("DDDD", sessionId)
////                appConfig.setSessionId(sessionId)
//        }
//    }
//}