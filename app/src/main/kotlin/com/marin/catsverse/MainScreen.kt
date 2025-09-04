// =============================================================================
// Arquivo: com.marin.catsverse.MainScreen.kt
// Descrição: Tela principal que gerencia o layout da interface,
//            incluindo o drawer de navegação e a TopAppBar.
//            Permite a seleção de tema através de um menu na TopAppBar.
// =============================================================================
package com.marin.catsverse

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.marin.catsverse.dominio.PreferenciaTema
import com.marin.catsverse.ui.Icones
import com.marin.catsverse.ui.preferencias.PreferenciasViewModel
import com.marin.catsverse.ui.theme.CatsVerseTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    // Injeta o PreferenciasViewModel. Ele será usado para obter e atualizar o tema.
    preferenciasViewModel: PreferenciasViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var optionsMenuExpanded by remember { mutableStateOf(false) }

    // Coleta a preferência de tema atual do ViewModel
    val preferenciaTemaAtual by preferenciasViewModel.preferenciaTema.collectAsState()

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
                        WindowInsets.systemBars.only(WindowInsetsSides.Top)
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icones.Menu, contentDescription = stringResource(R.string.abrir_menu_de_navegacao))
                        }
                    },
                    actions = {
                        IconButton(onClick = { optionsMenuExpanded = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.mais_opcoes))
                        }

                        DropdownMenu(
                            expanded = optionsMenuExpanded,
                            onDismissRequest = { optionsMenuExpanded = false }
                        ) {
                            // Seção de Seleção de Tema dentro do DropdownMenu
                            Text(
                                text = "Tema do Aplicativo", // Título para a seção de tema
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            ThemeDropdownOption(
                                text = "Claro",
                                selected = preferenciaTemaAtual == PreferenciaTema.LIGHT,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.LIGHT)
                                    optionsMenuExpanded = false // Fecha o menu
                                }
                            )
                            ThemeDropdownOption(
                                text = "Escuro",
                                selected = preferenciaTemaAtual == PreferenciaTema.DARK,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.DARK)
                                    optionsMenuExpanded = false
                                }
                            )
                            ThemeDropdownOption(
                                text = "Padrão do Sistema",
                                selected = preferenciaTemaAtual == PreferenciaTema.SYSTEM,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.SYSTEM)
                                    optionsMenuExpanded = false
                                }
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                thickness = DividerDefaults.Thickness,
                                color = DividerDefaults.color
                            )

                            // Item de menu "Sobre"
                            val sobreRouteInfo = AppRoutes.routes.find { it.route == "sobre" }
                            sobreRouteInfo?.let { routeInfo ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = routeInfo.titleResId)) },
                                    onClick = {
                                        navController.navigate(routeInfo.route)
                                        optionsMenuExpanded = false
                                    }
                                )
                            }
                            // Adicione mais DropdownMenuItems aqui se necessário
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

/**
 * Composable auxiliar para exibir uma opção de tema no DropdownMenu.
 * Inclui um RadioButton e texto.
 */
@Composable
private fun ThemeDropdownOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = selected,
                    onClick = null // O clique é tratado pelo DropdownMenuItem
                )
                Spacer(Modifier.width(8.dp))
                Text(text)
            }
        },
        onClick = onClick // O clique no item inteiro dispara a ação
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    CatsVerseTheme {
        // Para o preview funcionar sem um ViewModel real, você pode precisar
        // criar um mock ou não passar o ViewModel aqui, mas isso limitaria
        // a funcionalidade do preview referente ao tema.
        // Para um preview simples da UI estática:
        MainScreen(preferenciasViewModel = hiltViewModel()) // Ou um mock ViewModel
    }
}

