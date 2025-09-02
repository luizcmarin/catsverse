// =============================================================================
// Arquivo: com.marin.catsverse.ui.theme.Color.kt
// Descrição: Define a paleta de cores para o tema CatsVerse em Jetpack Compose.
//            Inclui um conjunto de cores base e semânticas inspiradas no framework Bootstrap,
//            bem como suas variantes de cinza, ênfase de texto e fundos sutis.
//            Essas cores são então mapeadas para os slots de cores do Material Design 3
//            para criar os esquemas de cores claro (Light*) e escuro (Dark*)
//            utilizados no tema do aplicativo.
// =============================================================================
package com.marin.catsverse.ui.theme

import androidx.compose.ui.graphics.Color

// =================================================================================
// Definições de Cores Base Inspiradas no Bootstrap
// =================================================================================

// --- Cores Base do Bootstrap ---
// Estas são as cores nomeadas primárias do Bootstrap.

/** Cor Azul padrão do Bootstrap (#0d6efd). Usada como primária. */
val BsBlue = Color(0xFF0d6efd)
/** Cor Indigo padrão do Bootstrap (#6610f2). */
val BsIndigo = Color(0xFF6610f2)
/** Cor Roxa padrão do Bootstrap (#6f42c1). */
val BsPurple = Color(0xFF6f42c1)
/** Cor Rosa padrão do Bootstrap (#d63384). */
val BsPink = Color(0xFFd63384)
/** Cor Vermelha padrão do Bootstrap (#dc3545). Usada para Perigo (Danger). */
val BsRed = Color(0xFFdc3545)
/** Cor Laranja padrão do Bootstrap (#fd7e14). */
val BsOrange = Color(0xFFfd7e14)
/** Cor Amarela padrão do Bootstrap (#ffc107). Usada para Aviso (Warning). */
val BsYellow = Color(0xFFffc107)
/** Cor Verde padrão do Bootstrap (#198754). Usada para Sucesso (Success). */
val BsGreen = Color(0xFF198754)
/** Cor Teal (verde-azulado) padrão do Bootstrap (#20c997). */
val BsTeal = Color(0xFF20c997)
/** Cor Ciano padrão do Bootstrap (#0dcaf0). Usada para Informação (Info). */
val BsCyan = Color(0xFF0dcaf0)
/** Cor Preta pura (#000000). */
val BsBlack = Color(0xFF000000)
/** Cor Branca pura (#FFFFFF). */
val BsWhite = Color(0xFFFFFFFF)

// --- Tons de Cinza do Bootstrap ---
// Uma escala de cinzas que vai do mais claro (100) ao mais escuro (900).

/** Cinza Bootstrap 100 (#f8f9fa). Muito claro, usado como 'Light'. */
val BsGray100 = Color(0xFFf8f9fa)
/** Cinza Bootstrap 200 (#e9ecef). */
val BsGray200 = Color(0xFFe9ecef)
/** Cinza Bootstrap 300 (#dee2e6). */
val BsGray300 = Color(0xFFdee2e6)
/** Cinza Bootstrap 400 (#ced4da). */
val BsGray400 = Color(0xFFced4da)
/** Cinza Bootstrap 500 (#adb5bd). */
val BsGray500 = Color(0xFFadb5bd)
/** Cinza Bootstrap 600 (#6c757d). Usado como 'Secondary'. */
val BsGray600 = Color(0xFF6c757d)
/** Cinza Bootstrap 700 (#495057). */
val BsGray700 = Color(0xFF495057)
/** Cinza Bootstrap 800 (#343a40). Usado como fundo secundário no tema escuro. */
val BsGray800 = Color(0xFF343a40)
/** Cinza Bootstrap 900 (#212529). Usado como 'Dark' e fundo do corpo no tema escuro. */
val BsGray900 = Color(0xFF212529)

// --- Cores Semânticas do Bootstrap ---
// Mapeamento das cores base para papéis semânticos comuns no Bootstrap.

/** Cor Primária do Bootstrap (baseada em [BsBlue]). */
val BsPrimary = BsBlue      // #0d6efd
/** Cor Secundária do Bootstrap (baseada em [BsGray600]). */
val BsSecondary = BsGray600 // #6c757d
/** Cor de Sucesso do Bootstrap (baseada em [BsGreen]). */
val BsSuccess = BsGreen     // #198754
/** Cor de Informação do Bootstrap (baseada em [BsCyan]). */
val BsInfo = BsCyan         // #0dcaf0
/** Cor de Aviso do Bootstrap (baseada em [BsYellow]). */
val BsWarning = BsYellow    // #ffc107
/** Cor de Perigo do Bootstrap (baseada em [BsRed]). */
val BsDanger = BsRed        // #dc3545
/** Cor Clara (Light) do Bootstrap (baseada em [BsGray100]). */
val BsLight = BsGray100     // #f8f9fa
/** Cor Escura (Dark) do Bootstrap (baseada em [BsGray900]). */
val BsDark = BsGray900      // #212529

// --- Cores de Ênfase de Texto do Bootstrap (Exemplos) ---
// Usadas para garantir contraste de texto sobre fundos coloridos.
// Estas são amostras; o Bootstrap tem uma gama mais ampla para diferentes fundos.

/** Cor de ênfase de texto para [BsPrimary] em tema claro (#052c65). */
val BsPrimaryTextEmphasisLight = Color(0xFF052c65)
/** Cor de ênfase de texto para [BsSecondary] em tema claro (#2b2f32). */
val BsSecondaryTextEmphasisLight = Color(0xFF2b2f32)
// ... (outras cores de ênfase de texto para tema claro seriam definidas aqui se usadas)

/** Cor de ênfase de texto para [BsPrimary] em tema escuro (#6ea8fe). */
val BsPrimaryTextEmphasisDark = Color(0xFF6ea8fe)
/** Cor de ênfase de texto para [BsSecondary] em tema escuro (#a7acb1). */
val BsSecondaryTextEmphasisDark = Color(0xFFa7acb1)
// ... (outras cores de ênfase de texto para tema escuro seriam definidas aqui se usadas)


// --- Cores de Fundo Sutil do Bootstrap (Exemplos) ---
// Usadas para fundos de containers ou elementos que precisam de um leve destaque.

/** Cor de fundo sutil para [BsPrimary] em tema claro (#cfe2ff). */
val BsPrimaryBgSubtleLight = Color(0xFFcfe2ff)
/** Cor de fundo sutil para [BsSecondary] em tema claro (#e2e3e5). */
val BsSecondaryBgSubtleLight = Color(0xFFe2e3e5)
// ... (outras cores de fundo sutil para tema claro seriam definidas aqui se usadas)

/** Cor de fundo sutil para [BsPrimary] em tema escuro (#031633). */
val BsPrimaryBgSubtleDark = Color(0xFF031633)
/** Cor de fundo sutil para [BsSecondary] em tema escuro (#161719). */
val BsSecondaryBgSubtleDark = Color(0xFF161719)
// ... (outras cores de fundo sutil para tema escuro seriam definidas aqui se usadas)

// --- FIM das Cores Base do Bootstrap ---

// =================================================================================
// Mapeamento das Cores Bootstrap para os Slots do Material Design 3
// =================================================================================

// --- Tema CLARO (Light Theme) ---
// Cores para o esquema claro do MaterialTheme, usando as definições Bootstrap acima.

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#b03fc9a9-3047-4952-8d76-6143329f7daa) */
val LightPrimary = BsPrimary // #0d6efd
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ac00ca8d-195b-4277-9204-c5a2b6214589) */
val LightOnPrimary = BsWhite // Texto branco sobre azul primário. Alternativa: BsPrimaryTextEmphasisLight.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#272e6fbf-x0673-42e7-9154-1b12b67f18b6) */
val LightPrimaryContainer = BsPrimaryBgSubtleLight // #cfe2ff
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#a8a86e92-e421-4f93-ad85-c513dfd59828) */
val LightOnPrimaryContainer = BsPrimaryTextEmphasisLight // #052c65

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#84dd69d3-b1c0-4389-8a18-6c845b4c4897) */
val LightSecondary = BsSecondary // #6c757d
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#2c322d64-e1e5-4eec-86aa-2dd07b822d48) */
val LightOnSecondary = BsWhite   // Alternativa: BsSecondaryTextEmphasisLight.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#3274534f-03f7-41e9-8664-9b8705353d21) */
val LightSecondaryContainer = BsSecondaryBgSubtleLight // #e2e3e5
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#b074477c-a7cb-4f36-a65c-423530864380) */
val LightOnSecondaryContainer = BsSecondaryTextEmphasisLight // #2b2f32

// Bootstrap não possui um "terciário" direto. BsTeal é usado como exemplo.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#147f2122-8356-4297-ba79-a720610141f1) */
val LightTertiary = BsTeal // #20c997
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f58c740c-2dcc-430c-bf0d-f5aa7c9f801e) */
val LightOnTertiary = BsWhite
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#a2b0561a-0359-4674-9f20-fe178fd83a87) */
val LightTertiaryContainer = Color(0xFFccebe4) // Tom mais claro de BsTeal (Exemplo).
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#fa42a11b-31d7-46bb-9f5e-daa9f6261556) */
val LightOnTertiaryContainer = Color(0xFF00382b) // Tom mais escuro de BsTeal para texto (Exemplo).

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ac3e70d4-ac9b-449e-b9e7-789a3f20ba8d) */
val LightError = BsDanger // #dc3545
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#4e7a83d7-27b2-44df-9191-1775e11449c3) */
val LightOnError = BsWhite
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#dd01c9ab-2b72-466d-ba13-a859427b9c9f) */
val LightErrorContainer = Color(0xFFf8d7da) // Bootstrap: --bs-danger-bg-subtle
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#c9f1a070-5b1b-4155-a226-17b8480356fd) */
val LightOnErrorContainer = Color(0xFF58151c) // Bootstrap: --bs-danger-text-emphasis

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#0572b9a1-c438-4796-98eb-d35229551e5e) */
val LightBackground = BsWhite // Bootstrap: --bs-body-bg (claro) #fff
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#125dbb26-9e67-42c2-8302-385d7f1d5d28) */
val LightOnBackground = BsGray900 // Bootstrap: --bs-body-color (claro) #212529

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#d0b961d1-1ae4-4bb0-b74a-a30282b9e696) */
val LightSurface = BsWhite // Frequentemente igual ao background no tema claro.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#57b54a7c-12b2-45a7-938a-86c0e633d7d7) */
val LightOnSurface = BsGray900
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#06e4a2fc-386b-43ac-93a0-f2150937a4c7) */
val LightSurfaceVariant = BsGray200 // #e9ecef (cinza claro para variação de superfície)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#c7bd2b6f-4029-43c3-b7de-8d070b4718c0) */
val LightOnSurfaceVariant = BsGray800 // #343a40 (texto escuro sobre cinza claro)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#52c00d41-f76d-4959-9bf1-d30906232230) */
val LightOutline = BsGray400 // #ced4da (para bordas)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f557ab42-a89e-4c75-8eb3-290fee7bf594) */
val LightOutlineVariant = BsGray300 // #dee2e6 (variação de borda mais clara)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#2f51887e-1510-449e-b9b0-379659b81358) */
val LightScrim = Color(0x99000000) // Preto com 60% de opacidade (padrão Compose: 0x99000000)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#044f5147-3f36-4d89-9e80-1a73982e0bf4) */
val LightInverseSurface = BsDark    // #212529 (Superfície escura para elementos invertidos no tema claro)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f7f7fa51-3701-4cf1-85b5-e6a3818e6128) */
val LightInverseOnSurface = BsLight // #f8f9fa (Texto claro sobre superfície invertida escura)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#d077c576-9218-4794-811c-c2b64a275b28) */
val LightInversePrimary = BsPrimary // Mantendo o primário ou um tom mais claro.

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ebc35061-0005-4c6e-8022-b2f703e30e70) */
val LightSurfaceTint = LightPrimary // Padrão é usar a cor primária para tint.


// --- Tema ESCURO (Dark Theme) ---
// Cores para o esquema escuro do MaterialTheme, usando as definições Bootstrap acima
// e referências de [data-bs-theme=dark] do Bootstrap.

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#b03fc9a9-3047-4952-8d76-6143329f7daa) */
val DarkPrimary = BsPrimary // Bootstrap mantém a mesma cor primária. Alternativa: Color(0xFF6ea8fe) (BsPrimaryTextEmphasisDark).
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ac00ca8d-195b-4277-9204-c5a2b6214589) */
val DarkOnPrimary = BsWhite  // Se DarkPrimary = BsPrimary (#0d6efd), branco oferece bom contraste.
// Se DarkPrimary for mais claro (ex: #6ea8fe), use BsBlack ou BsGray900.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#272e6fbf-x0673-42e7-9154-1b12b67f18b6) */
val DarkPrimaryContainer = BsPrimaryBgSubtleDark // #031633
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#a8a86e92-e421-4f93-ad85-c513dfd59828) */
val DarkOnPrimaryContainer = BsPrimaryTextEmphasisDark // #6ea8fe

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#84dd69d3-b1c0-4389-8a18-6c845b4c4897) */
val DarkSecondary = BsSecondary // #6c757d (Bootstrap mantém). Alternativa: BsGray800 (#343a40, que é --bs-secondary-bg escuro).
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#2c322d64-e1e5-4eec-86aa-2dd07b822d48) */
val DarkOnSecondary = BsGray300 // Texto claro (#dee2e6) para contraste com BsSecondary ou BsGray800.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#3274534f-03f7-41e9-8664-9b8705353d21) */
val DarkSecondaryContainer = BsSecondaryBgSubtleDark // #161719
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#b074477c-a7cb-4f36-a65c-423530864380) */
val DarkOnSecondaryContainer = BsSecondaryTextEmphasisDark // #a7acb1

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#147f2122-8356-4297-ba79-a720610141f1) */
val DarkTertiary = BsTeal // Ou uma variação mais clara/escura de BsTeal para o tema escuro.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f58c740c-2dcc-430c-bf0d-f5aa7c9f801e) */
val DarkOnTertiary = BsBlack // Se DarkTertiary (BsTeal) for claro o suficiente.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#a2b0561a-0359-4674-9f20-fe178fd83a87) */
val DarkTertiaryContainer = Color(0xFF022a20) // Exemplo: tom mais escuro de Teal para container.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#fa42a11b-31d7-46bb-9f5e-daa9f6261556) */
val DarkOnTertiaryContainer = Color(0xFF6cdcc1) // Exemplo: tom mais claro de Teal para texto.

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ac3e70d4-ac9b-449e-b9e7-789a3f20ba8d) */
val DarkError = BsDanger // #dc3545 (Bootstrap mantém).
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#4e7a83d7-27b2-44df-9191-1775e11449c3) */
val DarkOnError = BsWhite
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#dd01c9ab-2b72-466d-ba13-a859427b9c9f) */
val DarkErrorContainer = Color(0xFF2c0b0e) // Bootstrap: --bs-danger-bg-subtle (escuro).
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#c9f1a070-5b1b-4155-a226-17b8480356fd) */
val DarkOnErrorContainer = Color(0xFFea868f) // Bootstrap: --bs-danger-text-emphasis (escuro).

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#0572b9a1-c438-4796-98eb-d35229551e5e) */
val DarkBackground = BsGray900 // Bootstrap: --bs-body-bg (escuro) #212529
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#125dbb26-9e67-42c2-8302-385d7f1d5d28) */
val DarkOnBackground = BsGray300 // Bootstrap: --bs-body-color (escuro) #dee2e6

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#d0b961d1-1ae4-4bb0-b74a-a30282b9e696) */
val DarkSurface = BsGray900 // Similar ao background no tema escuro.
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#57b54a7c-12b2-45a7-938a-86c0e633d7d7) */
val DarkOnSurface = BsGray300
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#06e4a2fc-386b-43ac-93a0-f2150937a4c7) */
val DarkSurfaceVariant = BsGray800 // #343a40 (um cinza um pouco mais claro que o fundo para variação)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#c7bd2b6f-4029-43c3-b7de-8d070b4718c0) */
val DarkOnSurfaceVariant = BsGray400 // #ced4da (texto mais claro sobre essa variação)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#52c00d41-f76d-4959-9bf1-d30906232230) */
val DarkOutline = BsGray700 // #495057 (para bordas no tema escuro)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f557ab42-a89e-4c75-8eb3-290fee7bf594) */
val DarkOutlineVariant = BsGray800 // #343a40 (variação de borda mais escura)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#2f51887e-1510-449e-b9b0-379659b81358) */
val DarkScrim = Color(0x99000000) // Preto com 60% de opacidade (padrão Compose: 0x99000000)

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#044f5147-3f36-4d89-9e80-1a73982e0bf4) */
val DarkInverseSurface = BsLight    // #f8f9fa (Superfície clara para elementos invertidos no tema escuro)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#f7f7fa51-3701-4cf1-85b5-e6a3818e6128) */
val DarkInverseOnSurface = BsDark   // #212529 (Texto escuro sobre superfície invertida clara)
/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#d077c576-9218-4794-811c-c2b64a275b28) */
val DarkInversePrimary = BsPrimary  // Mantendo o primário ou uma variação mais escura/clara.

/** [M3 Doc](https://m3.material.io/styles/color/the-color-system/color-roles#ebc35061-0005-4c6e-8022-b2f703e30e70) */
val DarkSurfaceTint = DarkPrimary // Padrão é usar a cor primária para tint.

