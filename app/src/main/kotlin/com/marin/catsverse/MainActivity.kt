// =============================================================================
// Arquivo: com.marin.catsverse.MainActivity.kt
// Descrição: Atividade principal que inicializa o aplicativo.
// =============================================================================
package com.marin.catsverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marin.catsverse.ui.theme.CatsVerseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatsVerseTheme {
                MainScreen()
            }
        }
    }
}