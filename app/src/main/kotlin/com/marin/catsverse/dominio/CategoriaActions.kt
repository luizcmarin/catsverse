// =============================================================================
// Arquivo: com.marin.catsverse.dominio.CategoriaActions.kt
// Descrição: Casos de uso/ações para a entidade Categoria.
// =============================================================================
package com.marin.catsverse.dominio

import com.marin.catsverse.R
import com.marin.catsverse.data.entity.Categoria
import com.marin.catsverse.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Ação para obter todos os registros do banco de dados em um fluxo.
 */
class ObterTodasCategoriasAction @Inject constructor(
    private val repository: CategoriaRepository
) {
    operator fun invoke(): Flow<List<Categoria>> {
        return repository.obterTodasCategoria()
    }
}

/**
 * Ação para salvar um registro no banco de dados.
 *
 * @throws ExcecaoApp se o nome da categoria for inválido.
 */
class SalvarCategoriaAction @Inject constructor(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria) {
        if (categoria.nome.isBlank()) {
            throw ExcecaoApp(R.string.erro_nome_vazio)
        }
        repository.salvarCategoria(categoria)
    }
}

/**
 * Ação para excluir um registro do banco de dados.
 */
class ExcluirCategoriaAction @Inject constructor(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria) {
        repository.excluirCategoria(categoria)
    }
}