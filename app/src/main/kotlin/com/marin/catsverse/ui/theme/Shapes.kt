// =============================================================================
// Arquivo: com.marin.catsverse.ui.theme.Shapes.kt
// Descrição: Define o sistema de formas do aplicativo, centralizando a definição
//            dos raios de canto para garantir uma consistência visual em todos
//            os componentes.
// =============================================================================
package com.marin.catsverse.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val BsShapes = Shapes(
    /**
     * Componentes muito pequenos.
     * @param 4.dp O arredondamento sutil para elementos como chips ou pequenos indicadores.
     */
    extraSmall = RoundedCornerShape(4.dp),

    /**
     * Componentes pequenos.
     * @param 6.dp O arredondamento padrão para elementos como botões e campos de texto.
     */
    small = RoundedCornerShape(6.dp),

    /**
     * Componentes de tamanho médio.
     * @param 8.dp O arredondamento ideal para cards, caixas de diálogo e elementos de lista.
     */
    medium = RoundedCornerShape(8.dp),

    /**
     * Componentes grandes.
     * @param 12.dp Um arredondamento mais proeminente para elementos maiores, como folhas
     * inferiores (bottom sheets).
     */
    large = RoundedCornerShape(12.dp),

    /**
     * Componentes muito grandes.
     * @param 16.dp O maior arredondamento para elementos de destaque na tela.
     */
    extraLarge = RoundedCornerShape(16.dp)
)

// =============================================================================
// Exemplo de Uso
// =============================================================================
/*
    Para usar essas formas, você deve passá-las para o seu tema:

    MaterialTheme(
        colorScheme = ...,
        typography = ...,
        shapes = BsShapes, // Passe aqui o seu objeto de formas
        content = content
    )

    Depois, em qualquer composable, você pode acessar a forma do tema:

    Card(
        shape = MaterialTheme.shapes.medium, // Usa o RoundedCornerShape(8.dp)
        onClick = { ... }
    ) {
        // ...
    }
*/