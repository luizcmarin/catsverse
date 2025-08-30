// =============================================================================
// Arquivo: com.marin.catsverse.data.dao.FormaPagamentoDao.kt
// Descrição: DAO para operações na tabela de formas de pagamento.
// =============================================================================
package com.marin.catsverse.data.dao

import androidx.room.*
import com.marin.catsverse.data.entity.FormaPagamentoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FormaPagamentoDao {

    /**
     * Insere uma nova forma de pagamento no banco de dados.
     * @param formaPagamento A entidade de forma de pagamento a ser inserida.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(formaPagamento: FormaPagamentoEntity)

    /**
     * Atualiza uma forma de pagamento existente no banco de dados.
     * @param formaPagamento A entidade de forma de pagamento a ser atualizada.
     */
    @Update
    suspend fun atualizar(formaPagamento: FormaPagamentoEntity)

    /**
     * Exclui uma forma de pagamento do banco de dados.
     * @param formaPagamento A entidade de forma de pagamento a ser excluída.
     */
    @Delete
    suspend fun excluir(formaPagamento: FormaPagamentoEntity)

    /**
     * Retorna todas as formas de pagamento do banco de dados em um fluxo.
     * @return Um Flow contendo uma lista de todas as FormaPagamentoEntity.
     */
    @Query("SELECT * FROM formas_pagamento")
    fun obterTodas(): Flow<List<FormaPagamentoEntity>>

    /**
     * Retorna uma forma de pagamento específica do banco de dados pelo seu ID.
     * @param id O ID da forma de pagamento a ser buscada.
     * @return A FormaPagamentoEntity com o ID correspondente, ou null se não for encontrada.
     */
    @Query("SELECT * FROM formas_pagamento WHERE id = :id")
    suspend fun obterPorId(id: Long): FormaPagamentoEntity?
}
