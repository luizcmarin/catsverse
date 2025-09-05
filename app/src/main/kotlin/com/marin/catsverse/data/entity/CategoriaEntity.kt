// =============================================================================
// Arquivo: com.marin.catsverse.data.entity.CategoriaEntity.kt
// Descrição: Entidade do Room para a tabela de categorias.
// =============================================================================
package com.marin.catsverse.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marin.catsverse.dominio.TipoReceitaDespesa

/**
 * Modelo de domínio que representa uma categoria na lógica de negócios da aplicação.
 * @property id O ID único da categoria.
 * @property nome O nome da categoria.
 * @property tipo O tipo da categoria (receita ou despesa).
 * @property icone O nome do ícone associado à categoria.
 * @property cor A representação inteira ARGB da cor associada à categoria.
 */
@Entity(
    tableName = "categorias",
    indices = [
        Index(value = ["nome"], unique = true),
        Index(value = ["tipo"])
    ]
)
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val nome: String,
    val tipo: TipoReceitaDespesa = TipoReceitaDespesa.DESPESA,
    val icone: String? = null,
    val cor: Int? = null
)

/**
 * Modelo de domínio usado na camada de negócios.
 */
data class Categoria(
    val id: Long? = null,
    val nome: String,
    val tipo: TipoReceitaDespesa = TipoReceitaDespesa.DESPESA,
    val icone: String? = null,
    val cor: Int? = null
)

/**
 * Converte a entidade do Room para o modelo de domínio.
 * @return O objeto de domínio correspondente.
 */
fun CategoriaEntity.toDomain(): Categoria {
    return Categoria(
        id = this.id,
        nome = this.nome,
        tipo = this.tipo,
        icone = this.icone,
        cor = this.cor
    )
}

/**
 * Converte o modelo de domínio para a entidade do Room.
 * @return O objeto de entidade do Room correspondente.
 */
fun Categoria.toEntity(): CategoriaEntity {
    return CategoriaEntity(
        id = this.id,
        nome = this.nome,
        tipo = this.tipo,
        icone = this.icone,
        cor = this.cor
    )
}