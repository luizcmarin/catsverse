// =============================================================================
// Arquivo: com.marin.catsverse.data.repository.CategoriaRepository.kt
// Descrição: Interface e implementação do repositório para a entidade Categoria.
// =============================================================================
package com.marin.catsverse.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.marin.catsverse.R
import com.marin.catsverse.data.dao.CategoriaDao
import com.marin.catsverse.data.entity.Categoria
import com.marin.catsverse.data.entity.toDomain
import com.marin.catsverse.data.entity.toEntity
import com.marin.catsverse.dominio.ExcecaoApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface CategoriaRepository {
    fun obterTodasCategoria(): Flow<List<Categoria>>
    suspend fun salvarCategoria(categoria: Categoria)
    suspend fun excluirCategoria(categoria: Categoria)
}

@Singleton
class CategoriaRepositoryImpl @Inject constructor(
    private val dao: CategoriaDao
) : CategoriaRepository {
    override fun obterTodasCategoria(): Flow<List<Categoria>> {
        return dao.obterTodas().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun salvarCategoria(categoria: Categoria) {
        try {
            dao.inserir(categoria.toEntity())
        } catch (e: SQLiteConstraintException) {
            throw ExcecaoApp(
                stringResId = R.string.erro_salvar,
                causaDoErro = e
            )
        }
    }

    override suspend fun excluirCategoria(categoria: Categoria) {
        dao.excluir(categoria.toEntity())
    }
}