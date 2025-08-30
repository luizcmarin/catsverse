// =============================================================================
// Arquivo: com.marin.catsverse.utils.Conversores.kt
// Descrição: Classe que fornece conversores de tipos para o Room.
// =============================================================================
package com.marin.catsverse.utils

import androidx.room.TypeConverter
import com.marin.catsverse.dominio.StatusTransacao
import com.marin.catsverse.dominio.TipoTransacao
import java.math.BigDecimal
import java.time.Instant

/**
 * Classe que fornece métodos para converter tipos de dados complexos que o Room
 * não consegue persistir diretamente, como `BigDecimal` e `Instant`.
 *
 */
class Conversores {

    // =====================================================================
    // Conversores para BigDecimal
    // =====================================================================
    /**
     * Converte uma String para um objeto BigDecimal.
     */
    @TypeConverter
    fun fromBigDecimalString(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    /**
     * Converte um BigDecimal para uma String simples.
     */
    @TypeConverter
    fun toBigDecimalString(bigDecimal: BigDecimal?): String? {
        return bigDecimal?.toPlainString()
    }

    // =====================================================================
    // Conversores para java.time.Instant
    // =====================================================================
    /**
     * Converte um Long (timestamp) para um objeto Instant.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    /**
     * Converte um objeto Instant para um Long (timestamp).
     */
    @TypeConverter
    fun toTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    // =====================================================================
    // Conversores para o enum TipoTransacao
    // =====================================================================
    /**
     * Converte uma String para o enum TipoTransacao.
     */
    @TypeConverter
    fun fromString(valor: String?): TipoTransacao? {
        return valor?.let { TipoTransacao.valueOf(it) }
    }

    /**
     * Converte o enum TipoTransacao para uma String (o nome do enum).
     */
    @TypeConverter
    fun toString(tipo: TipoTransacao?): String? {
        return tipo?.name
    }

    // =====================================================================
    // Conversores para o enum StatusTransacao
    // =====================================================================
    /**
     * Converte uma String para o enum StatusTransacao.
     */
    @TypeConverter
    fun fromStringStatusTransacao(valor: String?): StatusTransacao? {
        return valor?.let { StatusTransacao.valueOf(it) }
    }

    /**
     * Converte o enum StatusTransacao para uma String (o nome do enum).
     */
    @TypeConverter
    fun toStringStatusTransacao(tipo: StatusTransacao?): String? {
        return tipo?.name
    }
}