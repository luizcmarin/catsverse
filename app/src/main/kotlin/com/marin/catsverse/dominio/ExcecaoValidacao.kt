// =============================================================================
// Arquivo: com.marin.catsverse.dominio.ExcecaoValidacao.kt
// Descrição: Exceção de Domínio Personalizada para erros de validação.
// Permite fornecer mensagens de erro amigáveis e específicas do contexto,
// utilizando recursos de string para facilitar a internacionalização.
// =============================================================================
package com.marin.catsverse.dominio

import androidx.annotation.StringRes

/**
 * Exceção personalizada para indicar que uma regra de validação de negócio falhou.
 * Esta exceção é projetada para ser usada com recursos de string, permitindo
 * mensagens de erro localizadas e formatadas.
 *
 * @property stringResId O ID do recurso de string (R.string.<id>) que contém a mensagem de erro.
 * @property args Argumentos opcionais para formatar a string do recurso, se ela contiver placeholders (ex: %s, %d).
 */
class ExcecaoValidacao(
    @StringRes val stringResId: Int, // Garante que um ID de recurso de string válido seja passado
    vararg val args: Any // Permite argumentos de formatação para a string
) : Exception() // Herda de Exception, tornando-a uma exceção verificada padrão.
