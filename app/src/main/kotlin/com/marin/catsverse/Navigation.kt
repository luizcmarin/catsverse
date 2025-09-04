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
import com.marin.catsverse.ui.formasPagamento.FormaPagamentoScreen
import com.marin.catsverse.ui.screens.PrivacyPolicyScreen
import com.marin.catsverse.ui.screens.SobreScreen

// --- Definições de Rota ---
object NavDestinations {
    const val INICIO = "inicio"
    const val FORMA_PAGAMENTO = "forma_pagamento"
    const val TAREFAS = "tarefas"
    const val SOBRE = "sobre"
    const val POLITICA_PRIVACIDADE = "politica_privacidade"
}

// --- Estrutura de Dados para Itens de Menu ---
data class MenuItemData( // Renomeado para MenuItemData para evitar conflito se você tiver um Composable MenuItem
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
)

// --- Lista de Itens de Menu (para BottomNavigation, Drawer, etc.) ---
// Você pode ter listas diferentes para diferentes menus, se necessário.
object AppMenuItems {
    val bottomNavigationItems = listOf(
        MenuItemData(
            route = NavDestinations.INICIO,
            titleResId = R.string.titulo_inicio, // Certifique-se que R.string.titulo_inicio existe
            icon = Icones.Inicio // Certifique-se que Icones.Inicio existe
        ),
        MenuItemData(
            route = NavDestinations.FORMA_PAGAMENTO,
            titleResId = R.string.titulo_forma_pagamento, // E R.string.titulo_forma_pagamento
            icon = Icones.FormaPagamento // E Icones.FormaPagamento
        ),
        MenuItemData(
            route = NavDestinations.TAREFAS,
            titleResId = R.string.titulo_tarefas, // E R.string.titulo_tarefas
            icon = Icones.Tarefas // E Icones.Tarefas
        )
        // Adicione mais itens aqui conforme necessário para sua barra de navegação inferior
    )

    val drawerNavigationItems = listOf(
        MenuItemData(
            route = NavDestinations.INICIO,
            titleResId = R.string.titulo_inicio,
            icon = Icones.Inicio
        ),
        MenuItemData(
            route = NavDestinations.SOBRE,
            titleResId = R.string.titulo_sobre,
            icon = Icones.Sobre
        )
        // Adicione mais itens para a gaveta de navegação aqui
    )
    // Você pode adicionar outras listas para outros menus aqui
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
