// =============================================================================
// Arquivo: com.marin.catsverse.ui.categorias.CategoriaScreen.kt
// Descrição: Define a tela de UI para o gerenciamento de Categorias.
//            Inclui o Composable principal da tela, o layout do conteúdo,
//            o formulário de entrada e os itens da lista de categorias.
//            Esta tela interage com [CategoriaViewModel] para obter dados
//            e executar ações.
// =============================================================================
package com.marin.catsverse.ui.categorias

import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marin.catsverse.R
import com.marin.catsverse.data.entity.Categoria
import com.marin.catsverse.dominio.IconeCategoria
import com.marin.catsverse.dominio.TipoReceitaDespesa
import com.marin.catsverse.ui.Icones
import com.marin.catsverse.dominio.AppExposedDropdownMenu
import com.marin.catsverse.dominio.AppOutlinedTextField
import com.marin.catsverse.dominio.UiEvent
import kotlinx.coroutines.flow.collectLatest

/**
 * Tela principal para gerenciar Categorias.
 *
 * Exibe um formulário para adicionar/editar categorias e uma lista das categorias existentes.
 * Lida com a apresentação de diálogos de confirmação, snackbars/toasts para feedback ao usuário
 * e navegação.
 *
 * @param viewModel O [CategoriaViewModel] que fornece o estado e lida com a lógica de negócios.
 *                  Injetado automaticamente pelo Hilt.
 * @param onNavigateBack Callback para ser invocado quando a navegação para a tela anterior é solicitada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaScreen(
    viewModel: CategoriaViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    if (uiState.mostrarDialogoExclusao && uiState.itemParaExcluir != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cancelarExclusao() },
            title = { Text(text = stringResource(R.string.dialogo_confirmar_exclusao_titulo)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.dialogo_confirmar_exclusao_mensagem,
                        uiState.itemParaExcluir!!.nome
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmarExclusao() }) {
                    Text(stringResource(R.string.botao_excluir))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cancelarExclusao() }) {
                    Text(stringResource(R.string.botao_cancelar))
                }
            }
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val message = context.getString(event.messageResId, *event.args.toTypedArray())
                    snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
                }
                is UiEvent.ShowToast -> {
                    val message = context.getString(event.messageResId, *event.args.toTypedArray())
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
                UiEvent.NavigateUp -> onNavigateBack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.titulo_categorias)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icones.Voltar, contentDescription = stringResource(R.string.botao_voltar))
                    }
                },
                actions = {
                    if (uiState.formState.nome.isNotBlank() || uiState.formState.id != null) {
                        TextButton(onClick = { viewModel.limparFormulario() }) {
                            Text(stringResource(R.string.botao_limpar))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        CategoriaContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onNomeChange = viewModel::onNomeChange,
            onTipoChange = viewModel::onTipoChange,
            onIconeChange = viewModel::onIconeChange,
            onCorChange = viewModel::onCorChange,
            onSalvarClick = viewModel::salvarCategoria,
            onExcluirClick = viewModel::prepararParaExcluir,
            onEditarClick = viewModel::prepararParaEditar
        )
    }
}

@Composable
private fun CategoriaContent(
    modifier: Modifier = Modifier,
    uiState: CategoriaScreenUiState,
    onNomeChange: (String) -> Unit,
    onTipoChange: (TipoReceitaDespesa) -> Unit,
    onIconeChange: (String) -> Unit,
    onCorChange: (Int?) -> Unit,
    onSalvarClick: () -> Unit,
    onExcluirClick: (Categoria) -> Unit,
    onEditarClick: (Categoria) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading && uiState.categorias.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        uiState.globalErrorResId?.let { errorResId ->
            Text(
                text = stringResource(errorResId),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        CategoriaInputForm(
            formState = uiState.formState,
            onNomeChange = onNomeChange,
            onTipoChange = onTipoChange,
            onIconeChange = onIconeChange,
            onCorChange = onCorChange,
            onSalvarClick = onSalvarClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!uiState.isLoading && uiState.categorias.isEmpty() && !uiState.formState.nome.isNotBlank()) {
            Text(
                text = stringResource(R.string.erro_nada_encontrado),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 32.dp)
            )
        } else if (uiState.categorias.isNotEmpty()) {
            Text(
                text = stringResource(R.string.label_categoria),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    items = uiState.categorias,
                    key = { categoria -> categoria.id ?: categoria.hashCode() }
                ) { categoria ->
                    CategoriaListItem(
                        categoria = categoria,
                        onExcluirClick = { onExcluirClick(categoria) },
                        onEditarClick = { onEditarClick(categoria) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun CategoriaInputForm(
    formState: CategoriaFormState,
    onNomeChange: (String) -> Unit,
    onTipoChange: (TipoReceitaDespesa) -> Unit,
    onIconeChange: (String) -> Unit,
    onCorChange: (Int?) -> Unit,
    onSalvarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(if (formState.id != null) R.string.botao_editar else R.string.botao_novo),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AppOutlinedTextField(
                value = formState.nome,
                onValueChange = onNomeChange,
                labelResId = R.string.label_nome,
                isError = formState.nomeErrorResId != null,
                supportingTextResId = formState.nomeErrorResId,
                enabled = !formState.isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para TIPO (Exemplo com Dropdown)
            AppExposedDropdownMenu(
                labelResId = R.string.label_tipo,
                options = TipoReceitaDespesa.entries,
                selectedOption = formState.tipo,
                onOptionSelected = { onTipoChange(it) },
                selectedOptionToString = { tipo -> stringResource(id = tipo.displayNameResId) }, // Chamada de stringResource aqui é OK, pois será usada por CategoriaInputForm
                dropdownItemContent = { tipo ->
                    // Você pode adicionar um Row aqui se quiser ícone + texto no item do menu, como no seu original
                    Text(stringResource(id = tipo.displayNameResId))
                },
                enabled = !formState.isSaving
                // Não precisa de leadingIconProvider aqui se o dropdownItemContent já lida com o ícone
            )
            formState.tipoErrorResId?.let { errorId ->
                Text(
                    text = stringResource(errorId),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppExposedDropdownMenu(
                labelResId = R.string.label_icone,
                options = IconeCategoria.entries,
                selectedOption = IconeCategoria.fromName(formState.iconeNome),
                onOptionSelected = { enumEntry -> onIconeChange(enumEntry.name) },selectedOptionToString = { icone -> stringResource(id = icone.displayNameResId) },
                dropdownItemContent = { icone ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = icone.icon, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(id = icone.displayNameResId))
                    }
                },
                // O leadingIconProvider original era para o TextField principal,
                // mas agora o dropdownItemContent pode lidar com o ícone do item.
                // Se você ainda quiser um ícone no TextField principal, ele deve ser passado
                // para o AppExposedDropdownMenu de forma diferente ou o leadingIconProvider
                // do AppExposedDropdownMenu deve ser usado apenas para o TextField.
                // Vou manter o leadingIconProvider para o TextField principal, como no seu original.
                leadingIconProvider = { enumEntry -> enumEntry.icon },
                enabled = !formState.isSaving
            )
            formState.iconeErrorResId?.let { errorId ->
                Text(
                    text = stringResource(errorId),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para COR (Placeholder - você precisará de um seletor de cor aqui)
            // Exemplo simples com um Box mostrando a cor e um TextField (não ideal para UX)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(formState.cor?.let { Color(it) } ?: Color.Gray)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .clickable { /* TODO: Abrir seletor de cor e chamar onCorChange */ }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.label_cor) + (formState.cor?.let { ": #${Integer.toHexString(it).uppercase()}" } ?: ": Nenhuma"),
                    modifier = Modifier.clickable { /* TODO: Abrir seletor de cor */ }
                )
                // AppColorPicker(...) ou um TextField para entrada manual, etc.
                // onCorChange seria chamado pelo seletor de cor.
            }
            formState.corErrorResId?.let { errorId ->
                Text(
                    text = stringResource(errorId),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSalvarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !formState.isSaving
            ) {
                if (formState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(if (formState.id != null) R.string.botao_atualizar else R.string.botao_salvar))
                }
            }
        }
    }
}

@Composable
private fun CategoriaListItem( // RENOMEADO
    categoria: Categoria, // TIPO CORRETO
    onExcluirClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val itemIcon = IconeCategoria.fromName(categoria.icone).icon // Usar IconeCategoria

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditarClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // Para exibir a cor
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(categoria.cor?.let { Color(it) } ?: Color.Transparent) // Cor da categoria
                .padding(2.dp) // Padding interno para o ícone não tocar as bordas do círculo de cor
        ) {
            Icon(
                imageVector = itemIcon,
                contentDescription = null, // Nome da categoria será o label principal
                modifier = Modifier.fillMaxSize(), // Ícone preenche o Box colorido
                tint = if (categoria.cor != null && categoria.cor != Color.Transparent.toArgb()) // Contraste do ícone
                    MaterialTheme.colorScheme.onPrimaryContainer // Ou uma cor que contraste bem com a cor da categoria
                else
                    MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categoria.nome,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(id = categoria.tipo.displayNameResId), // Supondo displayNameResId
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onExcluirClick) {
            Icon(
                imageVector = Icones.Lixeira,
                contentDescription = stringResource(R.string.botao_excluir, categoria.nome), // String mais específica
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


// =============================================================================
// Previews
// =============================================================================

@Preview(showBackground = true, name = "CategoriaScreen Preview")
@Composable
fun CategoriaScreenPreview() {
    MaterialTheme {
        // CategoriaScreen(onNavigateBack = {}) // Usaria ViewModel real
    }
}

@Preview(showBackground = true, name = "Conteúdo Vazio")
@Composable
fun CategoriaContentPreview_Empty() {
    MaterialTheme {
        CategoriaContent(
            uiState = CategoriaScreenUiState(isLoading = false, categorias = emptyList()),
            onNomeChange = {}, onTipoChange = {}, onIconeChange = {}, onCorChange = {},
            onSalvarClick = {}, onExcluirClick = {}, onEditarClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Conteúdo com Dados")
@Composable
fun CategoriaContentPreview_WithData() {
    val sampleData = listOf(
        Categoria(1, "Salário", TipoReceitaDespesa.RECEITA, IconeCategoria.DINHEIRO.name, Color.Green.toArgb()),
        Categoria(2, "Supermercado", TipoReceitaDespesa.DESPESA, IconeCategoria.CARTEIRA.name, Color.Red.toArgb())
    )
    MaterialTheme {
        CategoriaContent(
            uiState = CategoriaScreenUiState(isLoading = false, categorias = sampleData),
            onNomeChange = {}, onTipoChange = {}, onIconeChange = {}, onCorChange = {},
            onSalvarClick = {}, onExcluirClick = {}, onEditarClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Formulário Nova Categoria")
@Composable
fun CategoriaInputFormPreview_New() {
    MaterialTheme {
        CategoriaInputForm(
            formState = CategoriaFormState(),
            onNomeChange = {}, onTipoChange = {}, onIconeChange = {}, onCorChange = {}, onSalvarClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Formulário Editando Categoria")
@Composable
fun CategoriaInputFormPreview_Editing() {
    MaterialTheme {
        CategoriaInputForm(
            formState = CategoriaFormState(
                id = 1L,
                nome = "Aluguel",
                tipo = TipoReceitaDespesa.DESPESA,
                iconeNome = IconeCategoria.BOLETO.name,
                cor = Color.Magenta.toArgb()
            ),
            onNomeChange = {}, onTipoChange = {}, onIconeChange = {}, onCorChange = {}, onSalvarClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Formulário com Erro")
@Composable
fun CategoriaInputFormPreview_Error() {
    MaterialTheme {
        CategoriaInputForm(
            formState = CategoriaFormState(nomeErrorResId = R.string.erro_nome_vazio, corErrorResId = R.string.erro_cor_invalida),
            onNomeChange = {}, onTipoChange = {}, onIconeChange = {}, onCorChange = {}, onSalvarClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Item da Lista de Categoria")
@Composable
fun CategoriaListItemPreview() {
    MaterialTheme {
        CategoriaListItem(
            categoria = Categoria(1, "Lazer", TipoReceitaDespesa.DESPESA, IconeCategoria.PIX.name, Color.Cyan.toArgb()),
            onExcluirClick = { },
            onEditarClick = { }
        )
    }
}
