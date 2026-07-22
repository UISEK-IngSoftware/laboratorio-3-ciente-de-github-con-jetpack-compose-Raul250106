package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.ui.screens.LoginForm
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel

import ec.edu.uisek.githubclient.services.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // INICIALIZACIÓN CRÍTICA: Permite que el cliente de red acceda al token guardado
        RetrofitClient.init(this)
        
        val authService = AuthService(context = this)
        setContent {
            GithubClientTheme {
                val listViewModel: RepoListViewModel = viewModel()

                var currentScreen by remember { mutableStateOf(if (authService.isLoggedIn()) "repoList" else "login") }
                var selectedRepo by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
                    "login" -> LoginForm (
                        onLoginSuccess = {
                            // Al entrar, forzamos la carga de los repos del nuevo usuario
                            listViewModel.fetchRepos()
                            currentScreen = "repoList"
                        }
                    )
                    "repoList" -> RepoList (
                        onNavigateToForm = { repo ->
                            selectedRepo = repo
                            currentScreen = "repoForm"
                        },
                        onLogout = {
                            authService.logout()
                            currentScreen = "login"
                        }
                    )
                    "repoForm" -> RepoForm(
                        repository = selectedRepo,
                        onSaveSuccess = {
                            listViewModel.fetchRepos()
                            currentScreen = "repoList"
                        },
                        onBackClick = { currentScreen = "repoList" }
                    )
                }
            }
        }
    }
}