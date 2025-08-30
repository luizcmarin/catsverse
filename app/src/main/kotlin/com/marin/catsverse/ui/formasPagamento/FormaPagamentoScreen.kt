package com.marin.catsverse.ui.formasPagamento

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.marin.catsverse.app.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marin.catsverse.data.entity.FormaPagamento
import com.marin.catsverse.ui.Icones
import com.marin.catsverse.ui.common.UiEvent
import com.marin.catsverse.ui.common.AppExposedDropdownMenu
import com.marin.catsverse.ui.common.AppOutlinedTextField
import com.marin.catsverse.dominio.IconeFormaPagamento
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormaPagamentoScreen(
    viewModel: FormaPagamentoViewModel = viewModel(),
    onNavigateUp: () -> Unit // Para navegação "voltar"
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

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
                UiEvent.NavigateUp -> onNavigateUp()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.titulo_forma_pagamento)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icones.Voltar, contentDescription = stringResource(R.string.botao_voltar))
                    }
                },
                actions = {
                    // Se o formulário estiver preenchido (editando ou novo não salvo)
                    if (uiState.formState.nome.isNotBlank() || uiState.formState.id != null) {
                        TextButton(onClick = { viewModel.limparFormulario() }) {
                            Text(stringResource(R.string.botao_limpar))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        FormaPagamentoContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onNomeChange = viewModel::onNomeChange,
            onIconeChange = viewModel::onIconeChange,
            onSalvarClick = viewModel::salvarFormaPagamento,
            onExcluirClick = viewModel::excluirFormaPagamento,
            onEditarClick = viewModel::prepararParaEditar
        )
    }
}

@Composable
private fun FormaPagamentoContent(
    modifier: Modifier = Modifier,
    uiState: FormaPagamentoScreenUiState,
    onNomeChange: (String) -> Unit,
    onIconeChange: (String) -> Unit,
    onSalvarClick: () -> Unit,
    onExcluirClick: (FormaPagamento) -> Unit,
    onEditarClick: (FormaPagamento) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading && uiState.formasPagamento.isEmpty()) {
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

        FormaPagamentoInputForm(
            formState = uiState.formState,
            onNomeChange = onNomeChange,
            onIconeChange = onIconeChange,
            onSalvarClick = onSalvarClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!uiState.isLoading && uiState.formasPagamento.isEmpty() && !uiState.formState.nome.isNotBlank()) {
            Text(
                text = stringResource(R.string.erro_nada_encontrado),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 32.dp)
            )
        } else if (uiState.formasPagamento.isNotEmpty()){
            Text(
                text = stringResource(R.string.label_forma_pagamento),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    items = uiState.formasPagamento,
                    key = { fp -> fp.id ?: fp.hashCode() }
                ) { formaPagamento ->
                    FormaPagamentoListItem(
                        formaPagamento = formaPagamento,
                        onExcluirClick = { onExcluirClick(formaPagamento) },
                        onEditarClick = { onEditarClick(formaPagamento) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun FormaPagamentoInputForm(
    formState: FormaPagamentoFormState,
    onNomeChange: (String) -> Unit,
    onIconeChange: (String) -> Unit,
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

            AppExposedDropdownMenu(
                labelResId = R.string.label_icone,
                options = IconeFormaPagamento.entries,
                selectedOption = IconeFormaPagamento.fromName(formState.iconeNome),
                onOptionSelected = { enumEntry -> onIconeChange(enumEntry.name) },
                optionToString = { enumEntry -> enumEntry.name.replaceFirstChar { it.titlecase() } },
                leadingIconProvider = { enumEntry -> enumEntry.icon },
                enabled = !formState.isSaving
            )
            // Se houver erro específico para ícone, exibir aqui
            formState.iconeErrorResId?.let { errorId ->
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
private fun FormaPagamentoListItem(
    formaPagamento: FormaPagamento,
    onExcluirClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val itemIcon = formaPagamento.icone?.let { nomeIcone ->
        IconeFormaPagamento.fromName(nomeIcone)?.icon
    } ?: Icones.FormaPagamento // Fallback

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditarClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = itemIcon,
            contentDescription = formaPagamento.nome,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = formaPagamento.nome,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onExcluirClick) {
            Icon(
                imageVector = Icones.Lixeira,
                contentDescription = stringResource(R.string.botao_excluir, formaPagamento.nome),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FormaPagamentoScreenPreview() {
    MaterialTheme {
        // Para um preview funcional, você precisaria mockar o ViewModel ou passar um estado fixo.
        // FormaPagamentoScreen(onNavigateUp = {}) // Este vai usar o ViewModel real
    }
}

@Preview(showBackground = true)
@Composable
fun FormaPagamentoContentPreview_Empty() {
    MaterialTheme {
        FormaPagamentoContent(
            uiState = FormaPagamentoScreenUiState(isLoading = false, formasPagamento = emptyList()),
            onNomeChange = {}, onIconeChange = {}, onSalvarClick = {}, onExcluirClick = {}, onEditarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormaPagamentoContentPreview_WithData() {
    val sampleData = listOf(
        FormaPagamento(1, "Dinheiro", IconeFormaPagamento.CARTEIRA.name),
        FormaPagamento(2, "Cartão de Crédito", IconeFormaPagamento.CARTAO_CREDITO.name)
    )
    MaterialTheme {
        FormaPagamentoContent(
            uiState = FormaPagamentoScreenUiState(isLoading = false, formasPagamento = sampleData),
            onNomeChange = {}, onIconeChange = {}, onSalvarClick = {}, onExcluirClick = {}, onEditarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormaPagamentoInputFormPreview_New() {
    MaterialTheme {
        FormaPagamentoInputForm(
            formState = FormaPagamentoFormState(),
            onNomeChange = {}, onIconeChange = {}, onSalvarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormaPagamentoInputFormPreview_Editing() {
    MaterialTheme {
        FormaPagamentoInputForm(
            formState = FormaPagamentoFormState(id = 1L, nome = "Dinheiro Antigo", iconeNome = IconeFormaPagamento.DINHEIRO.name),
            onNomeChange = {}, onIconeChange = {}, onSalvarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormaPagamentoInputFormPreview_Error() {
    MaterialTheme {
        FormaPagamentoInputForm(
            formState = FormaPagamentoFormState(nomeErrorResId = R.string.erro_nome_vazio),
            onNomeChange = {}, onIconeChange = {}, onSalvarClick = {}
        )
    }
}
