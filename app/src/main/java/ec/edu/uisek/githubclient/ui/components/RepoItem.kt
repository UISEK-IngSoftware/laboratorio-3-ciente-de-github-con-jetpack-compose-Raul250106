package ec.edu.uisek.githubclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import org.intellij.lang.annotations.Language
import org.w3c.dom.Text

@Composable

fun RepoItem (
    repoName: String,
    repoDescription: String?,
    repoLanguage: String,
    repoImage: String
) {
    Card (
        modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row (
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) { AsyncImage(
                model = repoImage,
                contentDescription = "Avatar Github",
                modifier = Modifier.size( size = 60.dp),
                contentScale = ContentScale.Crop
                )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = repoName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                if (!repoDescription.isNullOrBlank()) {
                    Text(
                        text = repoDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
                Spacer(modifier = Modifier.height(height = 4.dp))
                if (!repoLanguage.isNullOrBlank()) {
                    Text(
                        text = repoLanguage,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun RepoItemPreview(){
    GithubClientTheme{
        RepoItem(
            "Laboratorio 3",
            "Practica",
            "Kotlin",
            "https://avatars.githubusercontent.com/u/216421919?s=400&v=4"
        )
    }
}