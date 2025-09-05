// =============================================================================
// Arquivo: com.marin.catsverse.data.dao.CategoriaDao.kt
// Descrição: DAO para operações na tabela de categorias.
// =============================================================================
package com.marin.catsverse.data.dao

import androidx.room.*
import com.marin.catsverse.data.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    /**
     * Insere uma nova categoria no banco de dados.
     * @param Categoria A entidade de categoria a ser inserida.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(Categoria: CategoriaEntity)

    /**
     * Atualiza uma categoria existente no banco de dados.
     * @param Categoria A entidade de categoria a ser atualizada.
     */
    @Update
    suspend fun atualizar(Categoria: CategoriaEntity)

    /**
     * Exclui uma categoria do banco de dados.
     * @param Categoria A entidade de categoria a ser excluída.
     */
    @Delete
    suspend fun excluir(Categoria: CategoriaEntity)

    /**
     * Retorna todas as categorias do banco de dados em um fluxo.
     * @return Um Flow contendo uma lista de todas as CategoriaEntity.
     */
    @Query("SELECT * FROM categorias")
    fun obterTodas(): Flow<List<CategoriaEntity>>

    /**
     * Retorna uma categoria específica do banco de dados pelo seu ID.
     * @param id O ID da categoria a ser buscada.
     * @return A CategoriaEntity com o ID correspondente, ou null se não for encontrada.
     */
    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun obterPorId(id: Long): CategoriaEntity?
}
