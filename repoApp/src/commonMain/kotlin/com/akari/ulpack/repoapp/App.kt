@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.akari.ulpack.repoapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.akari.ulpack.repo.repoapp.generated.resources.Res
import com.akari.ulpack.repo.repoapp.generated.resources.test
import com.akari.ulpack.repoapp.ui.theme.RepoTheme
import org.jetbrains.compose.resources.painterResource

val TEST_DATA = listOf(
    "Kiui",
    "Chaisto",
    "Jelee"
).flatMap { name -> List(10) { "$name$it" } }

@Composable
fun App() {
    RepoTheme {
        val uriHandler = LocalUriHandler.current
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Ulpack Repo")
                        },
                        actions = {
                            IconButton(
                                onClick = { uriHandler.openUri("https://github.com/iAkariAk/ulayout") }
                            ) {
                                Icon(Icons.Outlined.Home, contentDescription = "View in GitHub")
                            }
                        }
                    )
                },
            ) { padding ->
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                        .padding(padding),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TEST_DATA.forEach { item ->
                        UlpackSummary(
                            modifier = Modifier
                                .height(300.dp)
                                .width(300.dp)
                                .padding(8.dp),
                            name = item,
                            description = "不登校".repeat(10),
                            icon = painterResource(Res.drawable.test)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Ciallo World")
            }
        }
    }
}


@Composable
fun UlpackSummary(
    name: String,
    description: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.weight(0.2f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.size(80.dp),
                painter = icon,
                contentDescription = "face"
            )
            Column(
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
