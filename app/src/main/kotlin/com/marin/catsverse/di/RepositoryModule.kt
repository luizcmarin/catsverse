// =============================================================================
// Arquivo: com.marin.catsverse.di.RepositoryModule.kt
// Descrição: Módulo Hilt dedicado a fornecer as implementações para as
//            interfaces de repositório da aplicação.
// =============================================================================
package com.marin.catsverse.di

import com.marin.catsverse.data.repository.FormaPagamentoRepository
import com.marin.catsverse.data.repository.FormaPagamentoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que define como as interfaces de repositório devem ser injetadas
 * em toda a aplicação. Este módulo utiliza a anotação `@Binds` para informar
 * ao Hilt qual implementação concreta usar para uma determinada interface de repositório.
 *
 * As dependências fornecidas por este módulo são instaladas no [SingletonComponent],
 * o que significa que estarão disponíveis durante todo o ciclo de vida da aplicação
 * e, se escopadas como `@Singleton`, serão instâncias únicas.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFormaPagamentoRepository(
        impl: FormaPagamentoRepositoryImpl
    ): FormaPagamentoRepository

    // Adicione outros métodos @Binds para outros repositórios aqui.
    // Exemplo:
    // @Binds
    // @Singleton
    // abstract fun bindOutroRepository(
    //     impl: OutroRepositoryImpl
    // ): OutroRepository
}

