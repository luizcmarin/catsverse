// =============================================================================
// Arquivo: com.marin.catsverse.ui.categorias.CategoriaViewModel.kt
// Descrição: ViewModel para a tela de gerenciamento de Categorias.
//            Responsável por carregar, salvar, editar e excluir categorias,
//            bem como gerenciar o estado da UI do formulário e da lista.
//            Utiliza a classe de exceção unificada `ExcecaoApp` para tratamento de erros.
// =============================================================================
package com.marin.catsverse.ui.categorias

import android.graphics.Color
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marin.catsverse.R
import com.marin.catsverse.data.entity.Categoria
import com.marin.catsverse.dominio.IconeCategoria
import com.marin.catsverse.dominio.TipoReceitaDespesa
import com.marin.catsverse.dominio.ExcecaoApp
import com.marin.catsverse.dominio.ExcluirCategoriaAction
import com.marin.catsverse.dominio.ObterTodasCategoriasAction
import com.marin.catsverse.dominio.SalvarCategoriaAction
import com.marin.catsverse.dominio.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriaFormState(
    val id: Long? = null,
    val nome: String = "",
    @StringRes val nomeErrorResId: Int? = null,
    val tipo: TipoReceitaDespesa = TipoReceitaDespesa.DESPESA,
    @StringRes val tipoErrorResId: Int? = null,
    val iconeNome: String = IconeCategoria.CARTEIRA.name,
    @StringRes val iconeErrorResId: Int? = null,
    val cor: Int? = Color.TRANSPARENT,
    @StringRes val corErrorResId: Int? = null,
    val isSaving: Boolean = false
)

data class CategoriaScreenUiState(
    val categorias: List<Categoria> = emptyList(),
    val formState: CategoriaFormState = CategoriaFormState(),
    val isLoading: Boolean = false,
    val itemParaExcluir: Categoria? = null,
    val mostrarDialogoExclusao: Boolean = false,
    @StringRes val globalErrorResId: Int? = null
)

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val obterTodasCategoriasAction: ObterTodasCategoriasAction,
    private val salvarCategoriaAction: SalvarCategoriaAction,
    private val excluirCategoriaAction: ExcluirCategoriaAction
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaScreenUiState())
    val uiState: StateFlow<CategoriaScreenUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    init {
        loadCategorias()
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            obterTodasCategoriasAction()
                .onStart { _uiState.update { it.copy(isLoading = true, globalErrorResId = null) } }
                .catch { e ->
                    val errorResId = if (e is ExcecaoApp) e.stringResId else R.string.erro_carregar_registros
                    _uiState.update { it.copy(isLoading = false, globalErrorResId = errorResId) }
                    if (e !is ExcecaoApp) {
                        Log.e("CategoriaVM", "Erro ao carregar categorias", e)
                    }
                }
                .collectLatest { listaCategorias ->
                    _uiState.update { it.copy(isLoading = false, categorias = listaCategorias) }
                }
        }
    }

    fun prepararParaExcluir(categoria: Categoria) {
        _uiState.update {
            it.copy(
                itemParaExcluir = categoria,
                mostrarDialogoExclusao = true
            )
        }
    }

    fun confirmarExclusao() {
        val categoriaParaExcluir = _uiState.value.itemParaExcluir ?: return

        viewModelScope.launch {
            try {
                excluirCategoriaAction(categoriaParaExcluir)
                _eventFlow.emit(UiEvent.ShowToast(R.string.mensagem_excluido_com_sucesso))

                if (_uiState.value.formState.id == categoriaParaExcluir.id) {
                    limparFormulario()
                }
            } catch (e: ExcecaoApp) {
                _eventFlow.emit(UiEvent.ShowSnackbar(messageResId = e.stringResId))
                e.cause?.let { Log.e("CategoriaVM", "Erro ao excluir categoria: ${e.message}", it) }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.erro_excluir))
                Log.e("CategoriaVM", "Erro inesperado ao excluir categoria", e)
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

    fun onNomeChange(novoNome: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(nome = novoNome, nomeErrorResId = null),
                globalErrorResId = null
            )
        }
    }

    fun onTipoChange(novoTipo: TipoReceitaDespesa) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    tipo = novoTipo,
                    tipoErrorResId = null,
                    // Se a mudança de tipo afetar a validade da cor, limpar o erro de cor também
                    corErrorResId = if (novoTipo == TipoReceitaDespesa.DESPESA && it.formState.corErrorResId == R.string.erro_categoria_cor_obrigatoria_para_receita) null else it.formState.corErrorResId
                ),
                globalErrorResId = null
            )
        }
    }

    fun onIconeChange(novoIconeNome: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(iconeNome = novoIconeNome, iconeErrorResId = null),
                globalErrorResId = null
            )
        }
    }

    fun onCorChange(novaCor: Int?) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    cor = novaCor,
                    corErrorResId = null,
                    // Se a cor agora é válida para receita, limpar o erro específico
                    tipoErrorResId = if (it.formState.tipo == TipoReceitaDespesa.RECEITA && novaCor != null && novaCor != Color.TRANSPARENT && it.formState.corErrorResId == R.string.erro_categoria_cor_obrigatoria_para_receita) null else it.formState.tipoErrorResId
                ),
                globalErrorResId = null
            )
        }
    }

    fun prepararParaEditar(categoria: Categoria) {
        _uiState.update {
            it.copy(
                formState = CategoriaFormState(
                    id = categoria.id,
                    nome = categoria.nome,
                    tipo = categoria.tipo,
                    iconeNome = IconeCategoria.fromName(categoria.icone).name, // Usando enum para fallback e consistência
                    cor = categoria.cor
                ),
                globalErrorResId = null
            )
        }
    }

    fun limparFormulario() {
        _uiState.update {
            it.copy(formState = CategoriaFormState(), globalErrorResId = null) // Reseta para os padrões, incluindo iconeNome
        }
    }

    fun salvarCategoria() {
        val currentFormState = _uiState.value.formState
        if (currentFormState.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(formState = currentFormState.copy(isSaving = true), globalErrorResId = null) }

            try {
                if (currentFormState.nome.isBlank()) {
                    throw ExcecaoApp(R.string.erro_nome_vazio)
                }
                
                if (currentFormState.tipo == TipoReceitaDespesa.DESPESA && currentFormState.cor == null) {
                    throw ExcecaoApp(R.string.erro_categoria_cor_obrigatoria_para_despesa)
                }
                
                if (currentFormState.tipo == TipoReceitaDespesa.RECEITA &&
                    (currentFormState.cor == null || currentFormState.cor == Color.TRANSPARENT)) {
                    throw ExcecaoApp(R.string.erro_categoria_cor_obrigatoria_para_receita)
                }

                val categoriaParaSalvar = Categoria(
                    id = currentFormState.id,
                    nome = currentFormState.nome.trim(),
                    tipo = currentFormState.tipo,
                    icone = currentFormState.iconeNome,
                    cor = currentFormState.cor
                )

                salvarCategoriaAction(categoriaParaSalvar)

                _eventFlow.emit(UiEvent.ShowSnackbar(R.string.mensagem_sucesso_salvar))
                limparFormulario()

            } catch (e: ExcecaoApp) {
                var nomeError: Int? = null
                var tipoError: Int? = null
                var iconeError: Int? = null
                var corError: Int? = null
                var globalError: Int? = null

                when (e.stringResId) {
                    R.string.erro_nome_vazio -> nomeError = e.stringResId
                    R.string.erro_categoria_tipo_invalido -> tipoError = e.stringResId
                    R.string.erro_icone_invalido -> iconeError = e.stringResId
                    R.string.erro_cor_invalida -> corError = e.stringResId
                    R.string.erro_categoria_cor_obrigatoria_para_despesa -> corError = e.stringResId
                    R.string.erro_categoria_cor_obrigatoria_para_receita -> corError = e.stringResId
                    else -> globalError = e.stringResId
                }

                _uiState.update {
                    it.copy(
                        formState = currentFormState.copy(
                            isSaving = false,
                            nomeErrorResId = nomeError ?: it.formState.nomeErrorResId,
                            tipoErrorResId = tipoError ?: it.formState.tipoErrorResId,
                            iconeErrorResId = iconeError ?: it.formState.iconeErrorResId,
                            corErrorResId = corError ?: it.formState.corErrorResId
                        ),
                        globalErrorResId = globalError
                    )
                }
                e.cause?.let { Log.e("CategoriaVM", "Erro ao salvar categoria: ${e.message}", it) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        formState = currentFormState.copy(isSaving = false),
                        globalErrorResId = R.string.erro_operacao_falhou
                    )
                }
                Log.e("CategoriaVM", "Erro inesperado ao salvar categoria", e)
            } finally {
                if (_uiState.value.formState.isSaving) {
                    _uiState.update { it.copy(formState = it.formState.copy(isSaving = false)) }
                }
            }
        }
    }
}

