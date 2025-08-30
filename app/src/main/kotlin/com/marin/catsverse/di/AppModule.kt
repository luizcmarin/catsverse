// =============================================================================
// Arquivo: com.marin.catsverse.di.AppModule.kt
// Descrição: Módulo Hilt unificado para a injeção de dependência em toda a aplicação.
// =============================================================================
package com.marin.catsverse.di

import android.content.Context
import androidx.room.Room
import com.marin.catsverse.data.AppDatabase
import com.marin.catsverse.data.dao.*
import com.marin.catsverse.data.repository.FormaPagamentoRepository
import com.marin.catsverse.data.repository.FormaPagamentoRepositoryImpl
import com.marin.catsverse.dominio.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule { // Mantendo o nome AppModule conforme o nome do arquivo original [1]

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
            context.applicationContext, // Usar applicationContext aqui é uma boa prática
            AppDatabase::class.java,
            "catsverse-database" // Nome do arquivo do banco de dados
        )
            // Adicione aqui migrações se/quando você alterar a versão do banco de dados
            // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    /**
     * Fornece uma instância singleton de FormaPagamentoDao.
     */
    @Provides
    @Singleton
    fun provideFormaPagamentoDao(appDatabase: AppDatabase): FormaPagamentoDao {
        return appDatabase.formaPagamentoDao()
    }

    // Adicione outros provedores de DAO aqui conforme necessário
    // Exemplo:
    // @Provides
    // @Singleton
    // fun provideOutroDao(appDatabase: AppDatabase): OutroDao {
    //     return appDatabase.outroDao()
    // }

    // =========================================================================
    // PROVEDORES DE REPOSITÓRIOS
    // =========================================================================

    /**
     * Módulo abstrato para vincular interfaces de repositórios às suas implementações.
     * Alternativamente, este RepositoryModule poderia ser um arquivo/módulo de nível superior separado.
     */
    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule { // Se você preferir uma classe abstrata: abstract class RepositoryModule {
        @Binds
        @Singleton
        fun bindFormaPagamentoRepository( // Para @Binds, a função pode ser abstrata se a classe/interface do módulo for abstrata
            formaPagamentoRepositoryImpl: FormaPagamentoRepositoryImpl
        ): FormaPagamentoRepository

        // Adicione outros bindings de repositório aqui
        // @Binds
        // @Singleton
        // abstract fun bindOutroRepository(
        //     outroRepositoryImpl: OutroRepositoryImpl
        // ): OutroRepository
    }

    // =========================================================================
    // PROVEDORES DE CASOS DE USO (AÇÕES)
    // =========================================================================

    // Ações para Formas de Pagamento
    @Provides
    @Singleton
    fun provideObterTodasFormasPagamentoAction(repository: FormaPagamentoRepository): ObterTodasFormasPagamentoAction {
        return ObterTodasFormasPagamentoAction(repository)
    }

    @Provides
    @Singleton
    fun provideSalvarFormaPagamentoAction(repository: FormaPagamentoRepository): SalvarFormaPagamentoAction {
        return SalvarFormaPagamentoAction(repository)
    }

    @Provides
    @Singleton
    fun provideExcluirFormaPagamentoAction(repository: FormaPagamentoRepository): ExcluirFormaPagamentoAction {
        return ExcluirFormaPagamentoAction(repository)
    }

    // Adicione outros provedores de Casos de Uso (Ações) aqui
}
