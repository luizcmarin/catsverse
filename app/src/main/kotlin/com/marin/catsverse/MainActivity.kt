// =============================================================================
// Arquivo: com.marin.catsverse.MainActivity.kt
// Descrição: Atividade principal que inicializa o aplicativo.
// =============================================================================
package com.marin.catsverse

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.marin.catsverse.dominio.ThemePreference
import com.marin.catsverse.ui.theme.CatsVerseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Variável para controlar a visibilidade da splash screen padrão
    private var keepSplashScreenOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instala a splash screen. DEVE ser chamado antes de super.onCreate() ou setContent().
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Mantenha a splash screen padrão visível até que nossa UI personalizada (Lottie)
        // sinalize que está pronta.
        splashScreen.setKeepOnScreenCondition { keepSplashScreenOn }

        // Animação de saída para a splash screen PADRÃO (aquela com o ícone do Android).
        // Isso acontece quando keepSplashScreenOn se torna false.
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            // Exemplo de animação de fade out para a view da splash screen padrão
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenView,
                View.ALPHA,
                1f,
                0f
            )
            fadeOut.interpolator = AnticipateInterpolator()
            fadeOut.duration = 300L // Duração curta para a transição para a Lottie

            // Chame remove() quando a animação terminar.
            fadeOut.doOnEnd {
                splashScreenViewProvider.remove()
            }
            fadeOut.start()
        }

        setContent {
            // Lógica do tema (adapte se necessário)
            val userThemePreference = ThemePreference.SYSTEM // Exemplo, use sua lógica real
            val systemIsDark = isSystemInDarkTheme()
            val actualDarkTheme = when (userThemePreference) {
                ThemePreference.SYSTEM -> systemIsDark
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
            }



            // Chamada inicial de enableEdgeToEdge fora do LaunchedEffect
            // para aplicar o estilo assim que possível.
            // O LaunchedEffect ainda é útil se actualDarkTheme puder mudar dinamicamente
            // enquanto a activity está viva (embora raro para tema base).
            DisposableEffect(Unit) { // Ou simplesmente chame enableEdgeToEdge aqui diretamente
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb(),
                        detectDarkMode = { actualDarkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(), // Scrim transparente para modo claro
                        Color.Transparent.toArgb(), // Scrim transparente para modo escuro
                        detectDarkMode = { actualDarkTheme }
                    )
                )
                onDispose { }
            }

            // Se actualDarkTheme pode mudar e você quer que as barras reajam,
            // o LaunchedEffect ainda é bom.
            // Se actualDarkTheme é determinado uma vez no início,
            // chamar enableEdgeToEdge diretamente antes de CatsVerseTheme pode ser suficiente.
            // Para consistência com seu código original, mantendo LaunchedEffect:
            LaunchedEffect(actualDarkTheme) {
                enableEdgeToEdge( // Esta chamada pode ser redundante se a chamada no DisposableEffect já fez o trabalho
                    // e actualDarkTheme não muda. Mas não deve causar problemas.
                    statusBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb(),
                        detectDarkMode = { actualDarkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(),
                        Color.Transparent.toArgb(),
                        detectDarkMode = { actualDarkTheme }
                    )
                )
            }

            CatsVerseTheme(
                userThemePreference = userThemePreference,
                // dynamicColor = true // Se você tiver essa opção no seu tema
            ) {
                var showLottieSplash by remember { mutableStateOf(true) }

                if (showLottieSplash) {
                    AppEntry(
                        onAnimationFinish = {
                            showLottieSplash = false // Transita para o MainScreen
                        },
                        signalSplashScreenReadyToDismiss = {
                            // Este é o ponto onde dizemos à splash padrão que
                            // nossa UI personalizada (Lottie) está pronta para ser mostrada,
                            // então a splash padrão pode começar sua animação de saída.
                            this.keepSplashScreenOn = false // Use this. para acessar a propriedade da classe
                        }
                    )
                } else {
                    // Substitua Surface + MainScreen pelo seu Composable raiz do app,
                    // que provavelmente já está em MainScreen()
                    MainScreen()
                }
            }
        }
    }
}

/**
 * Composable para exibir a animação Lottie como parte da experiência de inicialização.
 */
@Composable
fun AppEntry(
    onAnimationFinish: () -> Unit,
    signalSplashScreenReadyToDismiss: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.catsverse_pata))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1, // Execute a animação uma vez
        speed = 1f      // Velocidade da animação
    )

    // Sinaliza que a UI está pronta para ser desenhada, então a splash padrão pode começar a sair.
    // Isso é chamado quando AppEntry começa a ser composto.
    LaunchedEffect(Unit) {
        signalSplashScreenReadyToDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Use a cor de fundo desejada
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize(0.7f) // Ajuste o tamanho da animação conforme necessário
        )
    }

    // Quando a animação Lottie terminar (progress == 1.0f para uma iteração),
    // chama onAnimationFinish para transitar para a tela principal.
    LaunchedEffect(progress) {
        if (progress == 1.0f) {
            onAnimationFinish()
        }
    }
}

