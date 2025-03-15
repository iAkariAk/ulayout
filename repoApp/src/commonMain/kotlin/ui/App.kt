@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.akari.ulpack.repoapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import ui.theme.RepoTheme

val TEST_DATA = listOf(
    "Kiui",
    "Chaisto",
    "Jelee"
)

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
                FlowColumn(
                    modifier = Modifier.padding(padding)
                ) {
                    TEST_DATA.forEach { item ->
                        OutlinedCard(
                            modifier = Modifier.height(250.dp)
                                .width(400.dp)
                        ) {
                            Row(
                                modifier = Modifier.weight(0.2f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    modifier = Modifier.size(30.dp),
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "face"
                                )
                                Column(
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "不登校",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
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
