package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

@Composable
fun RepoList () {
    Column (
        modifier = Modifier
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        RepoItem(
            "Laboratorio 1",
            "Jetpack compose",
            "Kotlin",
            "https://avatars.githubusercontent.com/u/216421919?s=400&v=4"
        )
        RepoItem(
            "Laboratorio 2",
            "Jetpack compose",
            "Kotlin",
            "https://avatars.githubusercontent.com/u/216421919?s=400&v=4"
        )
        RepoItem(
            "Laboratorio 3",
            "Jetpack compose",
            "Kotlin",
            "https://avatars.githubusercontent.com/u/216421919?s=400&v=4"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview () {
    GithubClientTheme {
        RepoList()
    }
}