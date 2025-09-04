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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.marin.catsverse.dominio.PreferenciaTema
import com.marin.catsverse.ui.Icones // Certifique-se que Icones.Menu está definido
import com.marin.catsverse.ui.preferencias.PreferenciasViewModel
import com.marin.catsverse.ui.theme.CatsVerseTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    preferenciasViewModel: PreferenciasViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var optionsMenuExpanded by remember { mutableStateOf(false) }
    val preferenciaTemaAtual by preferenciasViewModel.preferenciaTema.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.padding(top = 12.dp)) // Espaçamento no topo do drawer
                // Usa a lista AppMenuItems.drawerNavigationItems (ou a lista que você destinou para o drawer)
                AppMenuItems.drawerNavigationItems.forEach { menuItem ->
                    val itemTitle = stringResource(id = menuItem.titleResId)
                    NavigationDrawerItem(
                        icon = { Icon(menuItem.icon, contentDescription = itemTitle) },
                        label = { Text(text = itemTitle) },
                        selected = currentRoute == menuItem.route,
                        onClick = {
                            navController.navigate(menuItem.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
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
                            Icon(
                                Icones.Menu, // Certifique-se que Icones.Menu existe
                                contentDescription = stringResource(R.string.abrir_menu_de_navegacao)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { optionsMenuExpanded = true }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = stringResource(R.string.mais_opcoes)
                            )
                        }

                        DropdownMenu(
                            expanded = optionsMenuExpanded,
                            onDismissRequest = { optionsMenuExpanded = false }
                        ) {
                            Text(
                                text = stringResource(R.string.tema_do_aplicativo),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                            ThemeDropdownOption(
                                text = stringResource(R.string.tema_claro),
                                selected = preferenciaTemaAtual == PreferenciaTema.LIGHT,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.LIGHT)
                                    optionsMenuExpanded = false
                                }
                            )
                            ThemeDropdownOption(
                                text = stringResource(R.string.tema_escuro),
                                selected = preferenciaTemaAtual == PreferenciaTema.DARK,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.DARK)
                                    optionsMenuExpanded = false
                                }
                            )
                            ThemeDropdownOption(
                                text = stringResource(R.string.tema_padrao_sistema),
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

                            // Item de menu "Sobre" usando NavDestinations e AppMenuItems
                            // Encontra o item "Sobre" na lista de itens do drawer ou uma lista específica de itens de menu de opções
                            val sobreMenuItem = AppMenuItems.drawerNavigationItems.find { it.route == NavDestinations.SOBRE }
                            sobreMenuItem?.let { item ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = item.titleResId)) },
                                    onClick = {
                                        navController.navigate(item.route)
                                        optionsMenuExpanded = false
                                    },
                                    // Opcional: Adicionar ícone ao DropdownMenuItem também
                                    // leadingIcon = { Icon(item.icon, contentDescription = null) }
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
                    onClick = null
                )
                Spacer(Modifier.width(8.dp))
                Text(text)
            }
        },
        onClick = onClick
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    CatsVerseTheme {
        MainScreen(preferenciasViewModel = hiltViewModel())
    }
}

