// =============================================================================
// Arquivo: com.marin.catsverse.dominio.ExcecaoDominioPersonalizada.kt
// Descrição: Exceção de Domínio Personalizada. Permite fornecer mensagens de erro
//             mais amigáveis (usando stringResId) e específicas do contexto
//             do seu aplicativo.
//   stringResId: Int?: O ID do recurso de string para a mensagem de erro localizável.
//                      Pode ser nulo se uma mensagem literal for suficiente ou
//                      se o erro não tiver uma mensagem de UI direta.
//   mensagemLiteral: String? = null: Uma mensagem de erro literal, útil para logging
//                                  ou para casos onde um stringResId não é aplicável.
//   cause: Throwable? = null: Permite que você "envolva" a exceção original.
// =============================================================================
package com.marin.catsverse.dominio

import androidx.annotation.StringRes

open class ExcecaoDominioPersonalizada(
    @StringRes open val stringResId: Int?, // Propriedade adicionada
    val mensagemLiteral: String? = null,    // Para logging ou mensagens não localizadas
    override val cause: Throwable? = null   // Sobrescrevendo cause de Exception
) : Exception(mensagemLiteral, cause) {     // Passando mensagemLiteral para o construtor de Exception

    // Construtor secundário para conveniência, se você às vezes só tem stringResId
    constructor(
        @StringRes stringResId: Int,
        cause: Throwable? = null
    ) : this(stringResId = stringResId, mensagemLiteral = null, cause = cause)

    // Construtor secundário para manter compatibilidade com o uso anterior (apenas mensagem literal)
    // Embora o ideal seja migrar para usar stringResId para mensagens de UI.
    constructor(
        mensagemLiteral: String,
        cause: Throwable? = null
    ) : this(stringResId = null, mensagemLiteral = mensagemLiteral, cause = cause)
}
