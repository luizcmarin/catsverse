package com.marin.catsverse.ui.formasPagamento

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catsverse.app.R
import com.marin.catsverse.data.entity.FormaPagamento
import com.marin.catsverse.dominio.*
import com.marin.catsverse.ui.common.UiEvent
import com.marin.catsverse.dominio.IconeFormaPagamento
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormaPagamentoFormState(
    val id: Long? = null, // Para edição
    val nome: String = "",
    @StringRes val nomeErrorResId: Int? = null,
    val iconeNome: String = IconeFormaPagamento.CARTEIRA.name,
    @StringRes val iconeErrorResId: Int? = null, // Se houver validação para ícone
    val isSaving: Boolean = false
)

data class FormaPagamentoScreenUiState(
    val formasPagamento: List<FormaPagamento> = emptyList(),
    val formState: FormaPagamentoFormState = FormaPagamentoFormState(),
    val isLoading: Boolean = false,
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

    init {
        loadFormasPagamento()
    }

    private fun loadFormasPagamento() {
        viewModelScope.launch {
            obterTodasFormasPagamentoAction()
                .onStart { _uiState.update { it.copy(isLoading = true, globalErrorResId = null) } }
                .catch { e ->
                    // Log.e("FPViewModel", "Erro ao carregar formas de pagamento", e)
                    _uiState.update { it.copy(isLoading = false, globalErrorResId = R.string.erro_carregar_dados) }
                }
                .collectLatest { formas ->
                    _uiState.update { it.copy(isLoading = false, formasPagamento = formas) }
                }
        }
    }

    fun onNomeChange(novoNome: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(nome = novoNome, nomeErrorResId = null), globalErrorResId = null)
        }
    }

    fun onIconeChange(novoIconeNome: String) {
        _uiState.update {
            it.copy(formState = it.formState.copy(iconeNome = novoIconeNome, iconeErrorResId = null), globalErrorResId = null)
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
                globalErrorResId = null
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
                    id = currentFormState.id, // Nulo para novo, preenchido para editar
                    nome = currentFormState.nome.trim(),
                    icone = currentFormState.iconeNome
                )

                salvarFormaPagamentoAction(formaPagamentoParaSalvar)
                _eventFlow.emit(UiEvent.ShowSnackbar(
                    R.string.mensagem_sucesso
                ))
                limparFormulario() // Reseta o formulário após salvar

            } catch (e: ExcecaoValidacao) {
                // Assumindo que ExcecaoValidacao pode ter um fieldName para identificar o campo
                // ou você mapeia o e.stringResId para o campo correto.
                // Aqui, simplificaremos: se for erro de nome vazio, mostramos no campo nome.
                if (e.stringResId == R.string.erro_nome_vazio) {
                    _uiState.update {
                        it.copy(formState = currentFormState.copy(isSaving = false, nomeErrorResId = e.stringResId))
                    }
                } else {
                    // Para outras validações, poderia ser um erro global ou mapear para outros campos
                    _uiState.update {
                        it.copy(formState = currentFormState.copy(isSaving = false), globalErrorResId = e.stringResId)
                    }
                }
            } catch (e: ExcecaoDominioPersonalizada) {
                _uiState.update {
                    it.copy(formState = currentFormState.copy(isSaving = false), globalErrorResId = e.stringResId ?: R.string.erro_operacao_falhou)
                }
            } catch (e: Exception) { // Outros erros inesperados
                // Log.e("FPViewModel", "Erro inesperado ao salvar", e)
                _uiState.update {
                    it.copy(formState = currentFormState.copy(isSaving = false), globalErrorResId = R.string.erro_operacao_falhou)
                }
            } finally {
                // Certifique-se de que isSaving seja resetado mesmo se a coroutine for cancelada
                // Se o try/catch cobrir todos os caminhos, este update pode não ser necessário se já feito nos catches.
                if (_uiState.value.formState.isSaving) { // Checa se ainda está salvando
                    _uiState.update { it.copy(formState = it.formState.copy(isSaving = false)) }
                }
            }
        }
    }

    fun excluirFormaPagamento(formaPagamento: FormaPagamento) {
        viewModelScope.launch {
            try {
                excluirFormaPagamentoAction(formaPagamento)
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.mensagem_excluido_com_sucesso))
                // Se o item excluído estava sendo editado, limpe o formulário
                if (_uiState.value.formState.id == formaPagamento.id) {
                    limparFormulario()
                }
            } catch (e: ExcecaoDominioPersonalizada) {
                _eventFlow.emit(UiEvent.ShowSnackbar(e.stringResId ?: R.string.erro_excluir_falhou))
            } catch (e: Exception) {
                // Log.e("FPViewModel", "Erro ao excluir", e)
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.erro_excluir_falhou))
            }
        }
    }
}
