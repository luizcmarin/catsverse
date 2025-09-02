// =============================================================================
// Arquivo: com.marin.catsverse.dominio.Enums.kt
// Descrição: Coleção de enums utilitárias para a lógica de negócios e tipagem
//            de dados da aplicação.
// =============================================================================
package com.marin.catsverse.dominio

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.marin.catsverse.R
import com.marin.catsverse.ui.Icones

/**
 * Enumeração que define os tipos possíveis para transações:
 * [DESPESA] para saídas e [RECEITA] para entradas.
 */
enum class TipoTransacao(@StringRes val displayNameResId: Int) {
    DESPESA(R.string.label_despesa),
    RECEITA(R.string.label_receita)
}

/**
 * Enumeração que define os tipos de status para transações:
 * [PAGO] para transações concluídas e [PENDENTE] para transações a serem realizadas.
 */
enum class StatusTransacao(@StringRes val displayNameResId: Int) {
    PAGO(R.string.label_pago),
    PENDENTE(R.string.label_pendente)
}

// Enum para representar a escolha do usuário
enum class ThemePreference {
    SYSTEM, LIGHT, DARK
}

/**
 * Enumeração que representa os ícones disponíveis para uma forma de pagamento.
 *
 * @property displayNameResId O ID do recurso de string para o nome de exibição localizável.
 * @property icon A referência [ImageVector] do ícone correspondente no objeto [Icones].
 */
enum class IconeFormaPagamento(
    @StringRes val displayNameResId: Int,
    val icon: ImageVector
) {
    CARTEIRA(R.string.label_forma_pagamento_carteira, Icones.FormaPagamento),
    DINHEIRO(R.string.label_forma_pagamento_dinheiro, Icones.Dinheiro),
    CARTAO_CREDITO(R.string.label_forma_pagamento_cartao_credito, Icones.CartaoCredito),
    PIX(R.string.label_forma_pagamento_pix, Icones.QrCode),
    BOLETO(R.string.label_forma_pagamento_boleto, Icones.Boleto),
    DEPOSITO(R.string.label_forma_pagamento_transferencia, Icones.Transferencia);

    companion object {
        fun fromName(name: String?): IconeFormaPagamento {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: CARTEIRA // Fallback
        }
    }
}

