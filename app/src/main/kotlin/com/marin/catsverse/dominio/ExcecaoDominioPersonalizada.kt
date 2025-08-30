// =============================================================================
// Arquivo: com.marin.catsverse.dominio.ExcecaoDominioPersonalizada.kt
// Descrição: Exceção de Domínio Personalizada. Permite fornecer mensagens de erro
//             literais ou baseadas em recursos de string (stringResId),
//             e também encapsular a exceção original (causa).
//
// Construtores:
// 1. Principal: (stringResId: Int?, mensagemLiteral: String? = null, cause: Throwable? = null)
//    - Usa stringResId para mensagens localizáveis.
//    - Usa mensagemLiteral para logs ou mensagens não localizadas.
//    - A mensagem da Exception base será mensagemLiteral ou a mensagem da causa.
//
// 2. Apenas com stringResId: (stringResId: Int, cause: Throwable? = null)
//    - Conveniência para quando apenas um ID de recurso de string é necessário.
//
// 3. Apenas com mensagemLiteral: (mensagemLiteral: String, cause: Throwable? = null)
//    - Conveniência para quando apenas uma mensagem de erro literal é necessária.
// =============================================================================
package com.marin.catsverse.dominio

import androidx.annotation.StringRes

/**
 * Exceção de Domínio Personalizada.
 */
open class ExcecaoDominioPersonalizada(
    @StringRes open val stringResId: Int?,
    val mensagemLiteral: String? = null,
    cause: Throwable? = null
) : Exception(mensagemLiteral ?: cause?.message, cause) {

    /**
     * Construtor secundário para quando apenas um ID de recurso de string é necessário.
     */
    constructor(
        @StringRes stringResId: Int,
        cause: Throwable? = null
    ) : this(stringResId = stringResId, mensagemLiteral = null, cause = cause)

    /**
     * Construtor secundário para quando apenas uma mensagem de erro literal é necessária.
     */
    constructor(
        mensagemLiteral: String,
        cause: Throwable? = null
    ) : this(stringResId = null, mensagemLiteral = mensagemLiteral, cause = cause)
}
