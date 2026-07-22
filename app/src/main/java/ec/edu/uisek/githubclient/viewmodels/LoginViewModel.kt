package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    fun login(authService: AuthService, username: String, token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            
            // Primero guardamos temporalmente para que RetrofitClient lo use en la validación
            authService.saveAuth(username, token)
            
            try {
                // Intentamos obtener el perfil del usuario para validar el token
                val user = RetrofitClient.apiService.getUser()
                
                // Opcional: Validar que el username ingresado coincida con el del token
                if (user.login.equals(username, ignoreCase = true)) {
                    _loginSuccess.value = true
                } else {
                    authService.logout()
                    _errorMsg.value = "El token pertenece al usuario '${user.login}', no a '$username'."
                }
            } catch (e: retrofit2.HttpException) {
                authService.logout()
                if (e.code() == 401) {
                    _errorMsg.value = "Token inválido o expirado (Error 401)."
                } else {
                    _errorMsg.value = "Error del servidor (${e.code()}): ${e.message()}"
                }
            } catch (e: Exception) {
                authService.logout()
                _errorMsg.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _loginSuccess.value = false
        _errorMsg.value = null
    }
}
