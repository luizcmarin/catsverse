package com.marin.catsverse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun CatsVerseNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.routes[0].route,
        modifier = modifier
    ) {
        composable(AppRoutes.routes[0].route) { PlaceholderScreen("Início") }
        composable(AppRoutes.routes[1].route) { PlaceholderScreen("Financeiro") }
        composable(AppRoutes.routes[2].route) { PlaceholderScreen("Tarefas") }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Bem-vindo à tela de $text")
    }
}