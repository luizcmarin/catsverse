// =============================================================================
// Arquivo: com.marin.catsverse.data.repository.FormaPagamentoRepository.kt
// Descrição: Interface e implementação do repositório para a entidade FormaPagamento.
// =============================================================================
package com.marin.catsverse.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.marin.catsverse.data.dao.FormaPagamentoDao
import com.marin.catsverse.data.entity.FormaPagamento
import com.marin.catsverse.data.entity.toDomain
import com.marin.catsverse.data.entity.toEntity
import com.marin.catsverse.dominio.ExcecaoDominioPersonalizada
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface FormaPagamentoRepository {
    fun obterTodasFormasPagamento(): Flow<List<FormaPagamento>>
    suspend fun salvarFormaPagamento(formaPagamento: FormaPagamento)
    suspend fun excluirFormaPagamento(formaPagamento: FormaPagamento)
}

@Singleton
class FormaPagamentoRepositoryImpl @Inject constructor(
    private val dao: FormaPagamentoDao
) : FormaPagamentoRepository {
    override fun obterTodasFormasPagamento(): Flow<List<FormaPagamento>> {
        return dao.obterTodas().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun salvarFormaPagamento(formaPagamento: FormaPagamento) {
        try {
            dao.inserir(formaPagamento.toEntity())
        } catch (e: SQLiteConstraintException) {
            Log.e(
                "FormaPagamentoRepo",
                "Erro ao salvar forma de pagamento: ${formaPagamento.nome}",
                e
            )
            // Usando o construtor com 'mensagemLiteral'
            throw ExcecaoDominioPersonalizada(
                mensagemLiteral = "Não foi possível salvar a forma de pagamento '${formaPagamento.nome}' devido a uma restrição de dados (ex: nome duplicado).",
                cause = e
            )
        }
    }

    override suspend fun excluirFormaPagamento(formaPagamento: FormaPagamento) {
        dao.excluir(formaPagamento.toEntity())
    }
}