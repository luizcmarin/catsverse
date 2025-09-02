// =============================================================================
// Arquivo: com.marin.catsverse.data.AppDatabase.kt
// Descrição: Classe principal do banco de dados Room, consolidando todas as
//            entidades e DAOs da aplicação.
// =============================================================================
package com.marin.catsverse.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marin.catsverse.AppInfo
import com.marin.catsverse.data.dao.FormaPagamentoDao
import com.marin.catsverse.data.entity.FormaPagamentoEntity
import com.marin.catsverse.utils.Conversores

/**
 * A classe principal do banco de dados Room para a aplicação CatsVerse.
 *
 * Esta classe consolida todas as entidades (tabelas) e Objetos de Acesso a Dados (DAOs)
 * do projeto. A instanciação singleton desta classe é gerenciada pelo Hilt através de
 * um módulo de injeção de dependência (ex: AppModule).
 *
 * @property entities As classes de entidade incluídas neste banco de dados.
 * @property version O número da versão do banco de dados. Deve ser incrementado
 *                   ao alterar o esquema, e migrações devem ser fornecidas.
 * @property exportSchema Indica se o esquema do banco de dados deve ser exportado
 *                        para um arquivo JSON. Recomendado `true` para desenvolvimento
 *                        e testes de migração.
 */
@Database(
    entities = [
        FormaPagamentoEntity::class,
        // Adicione outras entidades aqui conforme necessário
    ],
    version = 1,
    exportSchema = AppInfo.ROOM_EXPORT_SCHEMA
)
@TypeConverters(Conversores::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun formaPagamentoDao(): FormaPagamentoDao
    // Adicione outros DAOs abstratos aqui conforme necessário

}
