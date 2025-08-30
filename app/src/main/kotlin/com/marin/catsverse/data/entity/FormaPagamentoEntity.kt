// =============================================================================
// Arquivo: com.marin.catsverse.data.entity.FormaPagamentoEntity.kt
// Descrição: Entidade do Room para a tabela de formas de pagamento.
// =============================================================================
package com.marin.catsverse.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidade do Room que representa a tabela 'formas_pagamento'.
 * @property id O ID único da forma de pagamento, gerado automaticamente.
 * @property nome O nome da forma de pagamento (ex: "Cartão de Crédito").
 * @property icone O nome do ícone associado à forma de pagamento.
 */
@Entity(
    tableName = "formas_pagamento",
    indices = [
        Index(value = ["nome"], unique = true),
    ]
)
data class FormaPagamentoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val nome: String,
    val icone: String? = null
)

/**
 * Modelo de domínio usado na camada de negócios.
 */
data class FormaPagamento(
    val id: Long? = null,
    val nome: String,
    val icone: String? = null
)

/**
 * Converte a entidade do Room para o modelo de domínio.
 * @return O objeto de domínio correspondente.
 */
fun FormaPagamentoEntity.toDomain(): FormaPagamento {
    return FormaPagamento(
        id = this.id,
        nome = this.nome,
        icone = this.icone
    )
}

/**
 * Converte o modelo de domínio para a entidade do Room.
 * @return O objeto de entidade do Room correspondente.
 */
fun FormaPagamento.toEntity(): FormaPagamentoEntity {
    return FormaPagamentoEntity(
        id = this.id,
        nome = this.nome,
        icone = this.icone
    )
}