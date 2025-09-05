// =============================================================================
// Arquivo: com.marin.catsverse.di.AppModule.kt
// Descrição: Módulo Hilt unificado para a injeção de dependência em toda a aplicação.
// =============================================================================
package com.marin.catsverse.di

import android.content.Context
import androidx.room.Room
import com.marin.catsverse.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.marin.catsverse.data.PreferenciasRepository
import com.marin.catsverse.data.dao.CategoriaDao
import com.marin.catsverse.data.dao.FormaPagamentoDao

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // =========================================================================
    // PROVEDORES DO BANCO DE DADOS E DAOS
    // =========================================================================

    /**
     * Fornece uma instância singleton de AppDatabase.
     * @param context O contexto da aplicação injetado pelo Hilt.
     * @return A instância única de AppDatabase.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "catsverse-database" // Nome do arquivo do banco de dados
        )
            // Adicione aqui migrações se/quando você alterar a versão do banco de dados
            // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    /**
     * Fornece uma instância singleton de Preferências.
     */
    @Singleton
    @Provides
    fun providePreferenciasRepository(@ApplicationContext context: Context): PreferenciasRepository {
        return PreferenciasRepository(context)
    }

    /**
     * Fornece uma instância singleton de FormaPagamentoDao.
     */
    @Provides
    @Singleton
    fun provideFormaPagamentoDao(appDatabase: AppDatabase): FormaPagamentoDao {
        return appDatabase.formaPagamentoDao()
    }

    /**
     * Fornece uma instância singleton de CategoriaDao.
     */
    @Provides
    @Singleton
    fun provideCategoriaDao(appDatabase: AppDatabase): CategoriaDao {
        return appDatabase.categoriaDao()
    }

}
