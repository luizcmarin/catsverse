package com.marin.catsverse

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class DrawerItem(val route: String, val title: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem("home", "Início", Icons.Default.Home),
        DrawerItem("finance", "Financeiro", Icons.Default.Info),
        DrawerItem("tasks", "Tarefas", Icons.Default.Info)
    )

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CatsVerse") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Chamando a função de navegação
            CatsVerseNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
