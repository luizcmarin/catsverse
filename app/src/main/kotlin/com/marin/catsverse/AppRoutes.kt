// =============================================================================
// Arquivo: com.marin.catsverse.AppRoutes.kt
// Descrição: Definições de rotas e dados de navegação para todo o aplicativo.
// =============================================================================
package com.marin.catsverse

import androidx.compose.ui.graphics.vector.ImageVector
import com.marin.catsverse.ui.Icones

data class AppRoute(val route: String, val title: String, val icon: ImageVector)

object AppRoutes {
    val routes = listOf(
        AppRoute("inicio", "Início", Icones.Inicio),
        AppRoute("financeiro", "Financeiro", Icones.Financeiro),
        AppRoute("tarefas", "Tarefas", Icones.Tarefas),
        AppRoute("sobre", "Sobre", Icones.Sobre),
        AppRoute("politica_privacidade", "Política de Privacidade", Icones.Privacidade)
    )
}