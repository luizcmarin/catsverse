// =============================================================================
// Arquivo: com.marin.catsverse.MainScreen.kt
// Descrição: Tela principal que gerencia o layout da interface,
//            incluindo o drawer de navegação e a TopAppBar.
//            Permite a seleção de tema através de um menu na TopAppBar.
// =============================================================================
package com.marin.catsverse

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.marin.catsverse.dominio.PreferenciaTema
import com.marin.catsverse.ui.Icones
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
    val currentDestination = navBackStackEntry?.destination

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var optionsMenuExpanded by remember { mutableStateOf(false) }
    val preferenciaTemaAtual by preferenciasViewModel.preferenciaTema.collectAsState()

    // Lista de rotas onde você NÃO quer mostrar o BottomNavigationBar
    // Você pode mover isso para AppMenuItems ou NavDestinations se preferir
    val routesWithoutBottomBar = listOf(
        NavDestinations.SOBRE,
        NavDestinations.POLITICA_PRIVACIDADE
        // Adicione outras rotas aqui se não quiser o BottomBar nelas
    )
    val shouldShowBottomBar = currentRoute !in routesWithoutBottomBar &&
            AppMenuItems.bottomNavigationItems.isNotEmpty()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.padding(top = 12.dp))
                AppMenuItems.drawerNavigationItems.forEach { menuItem ->
                    val itemTitle = stringResource(id = menuItem.titleResId)
                    NavigationDrawerItem(
                        icon = { Icon(menuItem.icon, contentDescription = itemTitle) },
                        label = { Text(text = itemTitle) },
                        selected = currentRoute == menuItem.route,
                        onClick = {
                            navController.navigate(menuItem.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
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
                                Icones.Menu,
                                contentDescription = stringResource(R.string.abrir_menu_de_navegacao)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { optionsMenuExpanded = true }) {
                            Icon(
                                Icones.TresPontosVertical,
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
                                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                            )

                            // Tema Claro
                            DropdownMenuItemWithCheck(
                                text = stringResource(R.string.tema_claro),
                                isSelected = preferenciaTemaAtual == PreferenciaTema.LIGHT,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.LIGHT)
                                    optionsMenuExpanded = false
                                }
                            )

                            // Tema Escuro
                            DropdownMenuItemWithCheck(
                                text = stringResource(R.string.tema_escuro),
                                isSelected = preferenciaTemaAtual == PreferenciaTema.DARK,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.DARK)
                                    optionsMenuExpanded = false
                                }
                            )

                            // Tema Padrão do Sistema
                            DropdownMenuItemWithCheck(
                                text = stringResource(R.string.tema_padrao_sistema),
                                isSelected = preferenciaTemaAtual == PreferenciaTema.SYSTEM,
                                onClick = {
                                    preferenciasViewModel.updatePreferenciaTema(PreferenciaTema.SYSTEM)
                                    optionsMenuExpanded = false
                                }
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = DividerDefaults.Thickness,
                                color = DividerDefaults.color
                            )

                            val sobreMenuItem = AppMenuItems.drawerNavigationItems.find { it.route == NavDestinations.SOBRE }
                            sobreMenuItem?.let { item ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = item.titleResId)) },
                                    onClick = {
                                        navController.navigate(item.route)
                                        optionsMenuExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                if (shouldShowBottomBar) {
                    NavigationBar(
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
                        )
                    ) {
                        AppMenuItems.bottomNavigationItems.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                                label = { Text(stringResource(screen.titleResId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            CatsVerseNavGraph(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

// Composable auxiliar para DropdownMenuItem com ícone de Check
@Composable
private fun DropdownMenuItemWithCheck(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text) },
        onClick = onClick,
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icones.Check,
                    contentDescription = stringResource(R.string.tema_atual_selecionado_descricao),
                    tint = MaterialTheme.colorScheme.primary // cor do ícone
                )
            } else {
                // Opcional: Adicionar um Spacer para alinhar os textos
                // Spacer(Modifier.width(24.dp)) // Largura aproximada de um ícone
            }
        }
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    CatsVerseTheme {
        MainScreen(preferenciasViewModel = hiltViewModel())
    }
}

