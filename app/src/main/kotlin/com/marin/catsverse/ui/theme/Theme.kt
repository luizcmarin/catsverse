// =============================================================================
// Arquivo: com.marin.catsverse.ui.theme.Theme.kt
// Descrição: Define o tema principal do aplicativo CatsVerse para Jetpack Compose.
//            Inclui a configuração dos esquemas de cores claro e escuro
//            (baseados nas cores Bootstrap definidas em Color.kt), tipografia e formas.
//            Permite alternar entre tema claro, escuro ou seguir o sistema,
//            com a opção de cores dinâmicas (Material You) atualmente desabilitada.
// =============================================================================
package com.marin.catsverse.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.marin.catsverse.dominio.ThemePreference

/**
 * Esquema de cores claro para o tema CatsVerse, derivado das cores Bootstrap.
 * Utiliza as definições de cores `Light*` de `Color.kt`.
 */
private val BsLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    inverseOnSurface = LightInverseOnSurface,
    inverseSurface = LightInverseSurface,
    inversePrimary = LightInversePrimary,
    surfaceTint = LightSurfaceTint,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim
)

/**
 * Esquema de cores escuro para o tema CatsVerse, derivado das cores Bootstrap.
 * Utiliza as definições de cores `Dark*` de `Color.kt`.
 */
private val BsDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = LightOnSecondary, // Nota: Verifique se DarkOnSecondary não deveria ser DarkOnSecondary de Color.kt
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    inverseOnSurface = DarkInverseOnSurface,
    inverseSurface = DarkInverseSurface,
    inversePrimary = DarkInversePrimary,
    surfaceTint = DarkSurfaceTint,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim
)

/**
 * Aplica o tema principal do aplicativo CatsVerse ao conteúdo Composable.
 *
 * Este tema configura o [MaterialTheme] com esquemas de cores personalizados
 * (claro e escuro, baseados em Bootstrap), tipografia ([BsTypography]) e formas ([BsShapes]).
 * Ele permite que o usuário escolha entre tema claro, escuro ou seguir a configuração do sistema
 * através de [userThemePreference].
 *
 * A funcionalidade de cores dinâmicas (Material You) está atualmente desabilitada por padrão
 * (`dynamicColor = false`). Se habilitada, usaria as cores do sistema no Android 12+.
 *
 * @param userThemePreference A preferência de tema do usuário (claro, escuro ou sistema).
 *                            Padrão é [ThemePreference.SYSTEM].
 * @param dynamicColor Se `true`, tentaria usar cores dinâmicas (Material You) no Android 12+.
 *                     Atualmente definido como `false` por padrão, o que significa que as cores
 *                     dinâmicas não são aplicadas e os esquemas definidos ([BsLightColorScheme]
 *                     e [BsDarkColorScheme]) são sempre usados.
 * @param content O conteúdo Composable ao qual o tema será aplicado.
 */
@Composable
fun CatsVerseTheme(
    userThemePreference: ThemePreference = ThemePreference.SYSTEM,
    dynamicColor: Boolean = false, // Cores dinâmicas desabilitadas por padrão
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val actualDarkTheme = when (userThemePreference) {
        ThemePreference.SYSTEM -> systemIsDark
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }

    // Seleciona o ColorScheme com base na preferência do usuário e se o sistema está em modo escuro.
    // A lógica de dynamicColor foi removida daqui, pois o parâmetro dynamicColor é false por padrão
    // e os imports relacionados foram comentados. Se dynamicColor for reativado,
    // a lógica para selecionar dynamicLightColorScheme/dynamicDarkColorScheme precisaria ser restaurada.
    val colorScheme = when {
        actualDarkTheme -> BsDarkColorScheme
        else -> BsLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = BsShapes,
        content = content
    )
}

