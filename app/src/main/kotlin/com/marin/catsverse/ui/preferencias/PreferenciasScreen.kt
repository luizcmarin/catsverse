// =============================================================================
// Arquivo: com.marin.catsverse.ui.preferencias.PreferenciasScreen.kt
// Descrição: Composable que define a tela de configurações de preferências
//            do aplicativo CatsVerse.
//            Permite ao usuário selecionar a preferência de tema do aplicativo
//            (claro, escuro ou padrão do sistema).
//            Utiliza o PreferenciasViewModel para obter o estado atual da//            preferência de tema e para solicitar atualizações quando o usuário
//            faz uma nova seleção.
// =============================================================================
package com.marin.catsverse.ui.preferencias

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme // Adicionado import para MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
// import androidx.lifecycle.viewmodel.compose.viewModel // hiltViewModel é geralmente preferido em projetos Hilt
import com.marin.catsverse.dominio.PreferenciaTema
// com.marin.catsverse.ui.preferencias.PreferenciasViewModel já está importado pelo wildcard

@Composable
fun PreferenciasScreen( // Adicionado parênteses na declaração da função
    // navController: NavController, // Descomente se for usar navegação para esta tela
    viewModel: PreferenciasViewModel = hiltViewModel()
) {
    // Coleta o estado da preferência de tema do ViewModel
    // Renomeado de currentThemeSetting para preferenciaTemaAtual para corresponder ao ViewModel
    val preferenciaTemaAtual by viewModel.preferenciaTema.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Escolha o Tema", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        // Opções de tema usando PreferenciaTema do domínio
        ThemeOptionRow(
            text = "Claro",
            selected = preferenciaTemaAtual == PreferenciaTema.LIGHT,
            onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.LIGHT) }
        )
        ThemeOptionRow(
            text = "Escuro",
            selected = preferenciaTemaAtual == PreferenciaTema.DARK,
            onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.DARK) }
        )
        ThemeOptionRow(
            text = "Padrão do Sistema",
            selected = preferenciaTemaAtual == PreferenciaTema.SYSTEM,
            onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.SYSTEM) }
        )

        // Exemplo alternativo com botões simples (mantido comentado como no original)
        // Spacer(Modifier.height(32.dp))
        // Button(onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.LIGHT) }) { Text("Tema Claro") }
        // Button(onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.DARK) }) { Text("Tema Escuro") }
        // Button(onClick = { viewModel.updatePreferenciaTema(PreferenciaTema.SYSTEM) }) { Text("Padrão do Sistema") }
    }
}

@Composable
fun ThemeOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

