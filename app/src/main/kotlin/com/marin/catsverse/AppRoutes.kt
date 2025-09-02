// =============================================================================
// Arquivo: com.marin.catsverseRoutes.kt
// Descrição: Definições de rotas e dados de navegação para todo o aplicativo.
// =============================================================================
package com.marin.catsverse

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.marin.catsverse.ui.Icones

data class AppRoute(val route: String, @StringRes val titleResId: Int, val icon: ImageVector)

object AppRoutes {
    val routes = listOf(
        AppRoute("inicio", R.string.titulo_inicio, Icones.Inicio),
        AppRoute("forma_pagamento", R.string.titulo_forma_pagamento, Icones.FormaPagamento),
        AppRoute("tarefas", R.string.titulo_tarefas, Icones.Tarefas),
        AppRoute("sobre", R.string.titulo_sobre, Icones.Sobre),
        AppRoute("politica_privacidade", R.string.titulo_politica_privacidade, Icones.Privacidade)
    )
}