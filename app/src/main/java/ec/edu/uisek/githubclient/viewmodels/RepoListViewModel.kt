package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel() {
    private val _repos = MutableStateFlow<List<Repository>> (value = emptyList())
    val repos : StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow (value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val deletingRepos = mutableSetOf<String>()

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val response = RetrofitClient.apiService.getRepository()
                _repos.value = response
            } catch (e: Exception) {
                _errorMsg.value = "Error durante carga de repositorios: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteRepository(owner: String, repoName: String) {
        val repoKey = "$owner/$repoName"
        if (deletingRepos.contains(repoKey)) return

        deletingRepos.add(repoKey)
        viewModelScope.launch {
            _errorMsg.value = null
            try {
                val response = RetrofitClient.apiService.deleteRepository(owner, repoName)
                
                if (response.isSuccessful) {
                    fetchRepos()
                } else {
                    val code = response.code()
                    if (code == 404) {
                        _errorMsg.value = "Error 404 en '$owner/$repoName': No encontrado. Verifica el permiso 'delete_repo'."
                    } else {
                        _errorMsg.value = "Error $code al borrar '$repoName': ${response.message()}"
                    }
                    fetchRepos() // Restaurar item
                }
            } catch (e: Exception) {
                _errorMsg.value = "Error de red: ${e.localizedMessage}"
                e.printStackTrace()
                fetchRepos() // Restaurar item
            } finally {
                deletingRepos.remove(repoKey)
            }
        }
    }
}
