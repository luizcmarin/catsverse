// =============================================================================
// Arquivo: com.marin.catsverse.ui.common.FormFields.kt
// Descrição: Componentes de UI reutilizáveis para formulários.
// =============================================================================
package com.marin.catsverse.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelResId: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    @StringRes supportingTextResId: Int? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(labelResId)) },
        isError = isError,
        supportingText = supportingTextResId?.let { { Text(stringResource(it)) } },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        enabled = enabled,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppExposedDropdownMenu(
    labelResId: Int,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionToString: (T) -> String, // Função para converter T para String para exibição
    modifier: Modifier = Modifier,
    leadingIconProvider: ((T) -> ImageVector?)? = null, // Opcional: Provedor de ícone para a opção selecionada
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOptionText = optionToString(selectedOption)

    ExposedDropdownMenuBox(
        expanded = expanded && enabled, // Só expande se habilitado
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(stringResource(labelResId)) },
            leadingIcon = leadingIconProvider?.let { provider ->
                provider(selectedOption)?.let { icon ->
                    { Icon(imageVector = icon, contentDescription = null) }
                }
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val optionText = optionToString(option)
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            leadingIconProvider?.let { provider ->
                                provider(option)?.let { icon ->
                                    Icon(imageVector = icon, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                }
                            }
                            Text(optionText)
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
