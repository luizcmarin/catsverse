// =============================================================================
// Arquivo: com.marin.catsverse.ui.formasPagamento.FormaPagamentoViewModel.kt
// Descrição: ViewModel para a tela de gerenciamento de Formas de Pagamento.
//            Responsável por carregar, salvar, editar e excluir formas de pagamento,
//            bem como gerenciar o estado da UI do formulário e da lista.
//            Utiliza a classe de exceção unificada `ExcecaoApp` para tratamento de erros.
// =============================================================================
package com.marin.catsverse.ui.formasPagamento

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catsverse.R
import com.marin.catsverse.data.entity.FormaPagamento
import com.marin.catsverse.dominio.ExcecaoApp
import com.marin.catsverse.dominio.IconeFormaPagamento
import com.marin.catsverse.dominio.ObterTodasFormasPagamentoAction
import com.marin.catsverse.dominio.SalvarFormaPagamentoAction
import com.marin.catsverse.dominio.ExcluirFormaPagamentoAction
import com.marin.catsverse.dominio.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormaPagamentoFormState(
    val id: Long? = null, // Para edição
    val nome: String = "",
    @StringRes val nomeErrorResId: Int? = null,
    val iconeNome: String = IconeFormaPagamento.CARTEIRA.name,
    @StringRes val iconeErrorResId: Int? = null,
    val isSaving: Boolean = false
)

data class FormaPagamentoScreenUiState(
    val formasPagamento: List<FormaPagamento> = emptyList(),
    val formState: FormaPagamentoFormState = FormaPagamentoFormState(),
    val isLoading: Boolean = false,
    val itemParaExcluir: FormaPagamento? = null,
    val mostrarDialogoExclusao: Boolean = false,
    @StringRes val globalErrorResId: Int? = null // Para erros não específicos de campo
)

@HiltViewModel
class FormaPagamentoViewModel @Inject constructor(
    private val obterTodasFormasPagamentoAction: ObterTodasFormasPagamentoAction,
    private val salvarFormaPagamentoAction: SalvarFormaPagamentoAction,
    private val excluirFormaPagamentoAction: ExcluirFormaPagamentoAction
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormaPagamentoScreenUiState())
    val uiState: StateFlow<FormaPagamentoScreenUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    fun prepararParaExcluir(formaPagamento: FormaPagamento) {
        _uiState.update {
            it.copy(
                itemParaExcluir = formaPagamento,
                mostrarDialogoExclusao = true
            )
        }
    }

    fun confirmarExclusao() {
        val formaParaExcluir = _uiState.value.itemParaExcluir ?: return // Retorna se não houver item

        viewModelScope.launch {
            try {
                // _uiState.update { it.copy(formState = it.formState.copy(isSaving = true)) }
                excluirFormaPagamentoAction(formaParaExcluir)
                _eventFlow.emit(UiEvent.ShowToast(R.string.mensagem_excluido_com_sucesso))

                if (_uiState.value.formState.id == formaParaExcluir.id) {
                    limparFormulario() // Limpa o formulário se o item excluído estava em edição
                }
            } catch (e: ExcecaoApp) {
                _eventFlow.emit(UiEvent.ShowSnackbar(messageResId = e.stringResId))
                e.cause?.let { Log.e("FormaPagamentoVM", "Erro ao excluir: ${e.message}", it) }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.erro_excluir))
                Log.e("FormaPagamentoVM", "Erro inesperado ao excluir", e)
            } finally {
                cancelarExclusao()
            }
        }
    }

    fun cancelarExclusao() {
        _uiState.update {
            it.copy(
                itemParaExcluir = null,
                mostrarDialogoExclusao = false
            )
        }
    }

    init {
        loadFormasPagamento()
    }

    private fun loadFormasPagamento() {
        viewModelScope.launch {
            obterTodasFormasPagamentoAction()
                .onStart { _uiState.update { it.copy(isLoading = true, globalErrorResId = null) } }
                .catch { e ->
                    // Para erros de carregamento, podemos emitir um evento para a UI
                    // ou atualizar o uiState com uma mensagem de erro global.
                    // Se a ação lançar ExcecaoApp, podemos ser mais específicos.
                    val errorResId = if (e is ExcecaoApp) e.stringResId else R.string.erro_carregar_registros
                    _uiState.update { it.copy(isLoading = false, globalErrorResId = errorResId) }
                    if (e !is ExcecaoApp) { // Log apenas para exceções não esperadas (ExcecaoApp já pode ter sido logada na origem)
                        Log.e("FormaPagamentoVM", "Erro ao carregar formas de pagamento", e)
                    }
                }
                .collectLatest { formas ->
                    _uiState.update { it.copy(isLoading = false, formasPagamento = formas) }
                }
        }
    }

    fun onNomeChange(novoNome: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(nome = novoNome, nomeErrorResId = null),
                globalErrorResId = null // Limpa erro global ao editar campo
            )
        }
    }

    fun onIconeChange(novoIconeNome: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(iconeNome = novoIconeNome, iconeErrorResId = null),
                globalErrorResId = null // Limpa erro global ao editar campo
            )
        }
    }

    fun prepararParaEditar(formaPagamento: FormaPagamento) {
        _uiState.update {
            it.copy(
                formState = FormaPagamentoFormState(
                    id = formaPagamento.id,
                    nome = formaPagamento.nome,
                    iconeNome = formaPagamento.icone ?: IconeFormaPagamento.CARTEIRA.name
                ),
                globalErrorResId = null // Limpa erro global ao iniciar edição
            )
        }
    }

    fun limparFormulario() {
        _uiState.update {
            it.copy(formState = FormaPagamentoFormState(), globalErrorResId = null)
        }
    }

    fun salvarFormaPagamento() {
        val currentFormState = _uiState.value.formState
        if (currentFormState.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(formState = currentFormState.copy(isSaving = true), globalErrorResId = null) }

            try {
                val formaPagamentoParaSalvar = FormaPagamento(
                    id = currentFormState.id,
                    nome = currentFormState.nome.trim(),
                    icone = currentFormState.iconeNome
                )

                salvarFormaPagamentoAction(formaPagamentoParaSalvar)

                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.mensagem_sucesso_salvar))
                limparFormulario()

            } catch (e: ExcecaoApp) {
                if (e.stringResId == R.string.erro_nome_vazio) {
                    _uiState.update {
                        it.copy(
                            formState = currentFormState.copy(
                                isSaving = false,
                                nomeErrorResId = e.stringResId
                            ),
                            globalErrorResId = null // Limpa erro global se for específico do nome
                        )
                    }
                } else {
                    // Para outras ExcecaoApp, trate como um erro global do formulário
                    _uiState.update {
                        it.copy(
                            formState = currentFormState.copy(isSaving = false),
                            globalErrorResId = e.stringResId
                        )
                    }
                }
                // Log da causa, se houver, para depuração
                e.cause?.let { Log.e("FormaPagamentoVM", "Erro ao salvar: ${e.message}", it) }

            } catch (e: Exception) { // Captura exceções genéricas inesperadas
                _uiState.update {
                    it.copy(
                        formState = currentFormState.copy(isSaving = false),
                        globalErrorResId = R.string.erro_operacao_falhou
                    )
                }
                Log.e("FormaPagamentoVM", "Erro inesperado ao salvar", e)
            } finally {
                // Certifique-se de que isSaving seja resetado, mesmo se a coroutine for cancelada
                // Se _uiState ainda indicar que está salvando, resete.
                if (_uiState.value.formState.isSaving) {
                    _uiState.update { it.copy(formState = it.formState.copy(isSaving = false)) }
                }
            }
        }
    }
}

