package com.marin.catsverse.ui.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Estado de um campo de formulário individual.
 *
 * @param T O tipo do valor do campo.
 * @property value O valor atual do campo.
 * @property errorResId O ID do recurso de string para a mensagem de erro, se houver.
 * @property onValueChange Callback para quando o valor do campo muda.
 */
data class FormFieldState<T>(
    val value: T,
    @StringRes val errorResId: Int? = null,
    val onValueChange: (T) -> Unit
)

// Função utilitária para criar um Text FormFieldState (String)
@Composable
fun rememberTextFieldState(initialValue: String = ""): MutableState<FormFieldState<String>> {
    return remember {
        mutableStateOf(
            FormFieldState(
                value = initialValue,
                onValueChange = { newValue ->
                    // A lógica de atualização real será feita no ViewModel
                    // Esta onValueChange aqui é mais para a interface do FormFieldState
                }
            )
        )
    }
}