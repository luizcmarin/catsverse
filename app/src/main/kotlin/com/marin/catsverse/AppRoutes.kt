package com.marin.catsverse

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class AppRoute(val route: String, val title: String, val icon: ImageVector)

object AppRoutes {
    val routes = listOf(
        AppRoute("inicio", "Início", Icons.Default.Home),
        AppRoute("finance", "Financeiro", Icons.Default.Info),
        AppRoute("tasks", "Tarefas", Icons.Default.Info)
    )
}