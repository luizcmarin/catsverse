// =============================================================================
// Arquivo: com.marin.catsverse.data.AppDatabase.kt
// Descrição: Classe principal do banco de dados Room, consolidando todas as entidades
// e DAOs da aplicação catsverse.
// =============================================================================
package com.marin.catsverse.data

// android.content.Context e androidx.room.Room não são mais estritamente necessários
// para a definição da classe AppDatabase em si, uma vez que a instanciação
// é feita pelo Hilt. No entanto, eles não causam dano se permanecerem.
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marin.catsverse.data.dao.FormaPagamentoDao
import com.marin.catsverse.data.entity.FormaPagamentoEntity
import com.marin.catsverse.utils.Conversores

/**
 * A classe principal do banco de dados Room.
 * O Hilt gerenciará a instanciação singleton desta classe através de um módulo de injeção.
 *
 * Esta classe consolida todas as entidades (tabelas) e DAOs do projeto.
 *
 * @property entities As classes de entidade incluídas neste banco de dados.
 * @property version O número da versão do banco de dados.
 * @property exportSchema Indica se o esquema do banco de dados deve ser exportado.
 */
@Database(
    entities = [
        FormaPagamentoEntity::class,
        // Adicione outras entidades aqui conforme necessário
    ],
    version = 1,
    exportSchema = true // Mantenha como true durante o desenvolvimento, considere false para releases se não precisar
)
@TypeConverters(Conversores::class) // Certifique-se de que a classe Conversores está correta e funcional
abstract class AppDatabase : RoomDatabase() {

    abstract fun formaPagamentoDao(): FormaPagamentoDao
    // Adicione outros DAOs abstratos aqui conforme necessário

}
