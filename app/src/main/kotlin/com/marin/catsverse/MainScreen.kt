// =============================================================================
// Arquivo: com.marin.catsverse.MainScreen.kt
// Descrição: Tela principal que gerencia o layout da interface,
//            incluindo o drawer de navegação e a TopAppBar.
// =============================================================================
package com.marin.catsverse

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.marin.catsverse.ui.Icones
import com.marin.catsverse.ui.theme.CatsVerseTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estado para controlar a expansão do menu de opções da TopAppBar
    var optionsMenuExpanded by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                AppRoutes.routes.forEach { item ->
                    val itemTitle = stringResource(id = item.titleResId)
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = itemTitle) },
                        label = { Text(text = itemTitle) },
                        selected = false,
                        onClick = {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                // Opcional: popUpTo para limpar backstack ou evitar múltiplas instâncias
                                // popUpTo(navController.graph.startDestinationId) {
                                //     saveState = true
                                // }
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
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(
                            WindowInsetsSides.Top
                        )
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icones.Menu, contentDescription = stringResource(R.string.abrir_menu_de_navegacao))
                        }
                    },
                    actions = {
                        // Ícone para o menu de opções (três pontos)
                        IconButton(onClick = { optionsMenuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.mais_opcoes))
                        }

                        // DropdownMenu para as opções
                        DropdownMenu(
                            expanded = optionsMenuExpanded,
                            onDismissRequest = { optionsMenuExpanded = false }
                        ) {
                            // ---- ITEM DE MENU PARA FINANCEIRO ----
                            // Acessando a rota "Financeiro" (assumindo que é routes[1] conforme AppRoutes.kt)
                           /* val financeiroRouteInfo = AppRoutes.routes[1]
                            DropdownMenuItem(
                                text = { Text(financeiroRouteInfo.title) },
                                onClick = {
                                    navController.navigate(financeiroRouteInfo.route)
                                    optionsMenuExpanded = false // Fecha o menu
                                }
                            )*/

                            val sobreRouteInfo = AppRoutes.routes.find { it.route == "sobre" }
                            // Verifica se a rota foi encontrada antes de usá-la
                            sobreRouteInfo?.let { routeInfo ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = routeInfo.titleResId)) },
                                    onClick = {
                                        navController.navigate(routeInfo.route)
                                        optionsMenuExpanded = false // Fecha o menu
                                    }
                                )
                            }
                            // Adicione mais itens de menu conforme necessário
                        }
                    }
                )
            }
        ) { innerPadding ->
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
    CatsVerseTheme {
        MainScreen()
    }
}

