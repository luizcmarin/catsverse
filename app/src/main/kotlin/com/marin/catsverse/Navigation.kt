// =============================================================================
// Arquivo: com.marin.catsverse.Navigation.kt
// Descrição: Gráfico de navegação, definições de rota e itens de menu.
// =============================================================================
package com.marin.catsverse

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.marin.catsverse.ui.Icones
import com.marin.catsverse.ui.categorias.CategoriaScreen
import com.marin.catsverse.ui.formasPagamento.FormaPagamentoScreen
import com.marin.catsverse.ui.screens.PrivacyPolicyScreen
import com.marin.catsverse.ui.screens.SobreScreen

// --- Definições de Rota ---
object NavDestinations {
    const val INICIO = "inicio"
    const val CATEGORIAS = "categorias"
    const val FORMA_PAGAMENTO = "forma_pagamento"
    const val TAREFAS = "tarefas"
    const val SOBRE = "sobre"
    const val POLITICA_PRIVACIDADE = "politica_privacidade"
}

data class MenuItemData(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
)

object AppMenuItems {
    val bottomNavigationItems = listOf(
        MenuItemData(
            route = NavDestinations.INICIO,
            titleResId = R.string.titulo_inicio,
            icon = Icones.Inicio
        ),
        MenuItemData(
            route = NavDestinations.FORMA_PAGAMENTO,
            titleResId = R.string.titulo_forma_pagamento,
            icon = Icones.FormaPagamento
        ),
        MenuItemData(
            route = NavDestinations.TAREFAS,
            titleResId = R.string.titulo_tarefas,
            icon = Icones.Tarefas
        )
    )

    // Adicione mais itens para a gaveta de navegação aqui
    val drawerNavigationItems = listOf(
        MenuItemData(
            route = NavDestinations.INICIO,
            titleResId = R.string.titulo_inicio,
            icon = Icones.Inicio
        ),
        MenuItemData(
            route = NavDestinations.CATEGORIAS,
            titleResId = R.string.titulo_categorias,
            icon = Icones.Categoria
        ),
        MenuItemData(
            route = NavDestinations.FORMA_PAGAMENTO,
            titleResId = R.string.titulo_forma_pagamento,
            icon = Icones.FormaPagamento
        ),
        MenuItemData(
            route = NavDestinations.SOBRE,
            titleResId = R.string.titulo_sobre,
            icon = Icones.Sobre
        )
    )
}


// --- Gráfico de Navegação ---
@Composable
fun CatsVerseNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.INICIO,
        modifier = modifier
    ) {
        composable(NavDestinations.INICIO) { PlaceholderScreen("Início") }

        composable(NavDestinations.CATEGORIAS) {
            CategoriaScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(NavDestinations.FORMA_PAGAMENTO) {
            FormaPagamentoScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(NavDestinations.TAREFAS) { PlaceholderScreen("Tarefas") }

        composable(NavDestinations.SOBRE) {
            SobreScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPrivacyPolicy = { navController.navigate(NavDestinations.POLITICA_PRIVACIDADE) }
            )
        }

        composable(NavDestinations.POLITICA_PRIVACIDADE) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// --- Tela de Exemplo ---
@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Bem-vindo à tela de $text")
    }
}
