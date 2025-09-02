// =============================================================================
// Arquivo: com.marin.catsverse.dominio.FormaPagamentoActions.kt
// Descrição: Casos de uso/ações para a entidade FormaPagamento.
// =============================================================================
package com.marin.catsverse.dominio

import com.marin.catsverse.R
import com.marin.catsverse.data.entity.FormaPagamento
import com.marin.catsverse.data.repository.FormaPagamentoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Ação para obter todas as formas de pagamento do banco de dados em um fluxo.
 */
class ObterTodasFormasPagamentoAction @Inject constructor(
    private val repository: FormaPagamentoRepository
) {
    operator fun invoke(): Flow<List<FormaPagamento>> {
        return repository.obterTodasFormasPagamento()
    }
}

/**
 * Ação para salvar uma nova forma de pagamento no banco de dados.
 *
 * @throws ExcecaoApp se o nome da forma de pagamento for inválido.
 */
class SalvarFormaPagamentoAction @Inject constructor(
    private val repository: FormaPagamentoRepository
) {
    suspend operator fun invoke(formaPagamento: FormaPagamento) {
        if (formaPagamento.nome.isBlank()) {
            throw ExcecaoApp(R.string.erro_nome_vazio)
        }
        repository.salvarFormaPagamento(formaPagamento)
    }
}

/**
 * Ação para excluir uma forma de pagamento do banco de dados.
 */
class ExcluirFormaPagamentoAction @Inject constructor(
    private val repository: FormaPagamentoRepository
) {
    suspend operator fun invoke(formaPagamento: FormaPagamento) {
        repository.excluirFormaPagamento(formaPagamento)
    }
}