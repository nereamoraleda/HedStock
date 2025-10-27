package es.cursos.android.ejercicios.stocksnma.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.login.SessionManager
import es.cursos.android.ejercicios.stocksnma.ui.navigation.Home
import es.cursos.android.ejercicios.stocksnma.ui.navigation.Login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    sessionManager: SessionManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination: MutableStateFlow<Any?> = _startDestination

    init {
        viewModelScope.launch {
            sessionManager.isLoggedIn.first {
                _startDestination.value = if (it) Home else Login
                true
            }
        }
    }
}