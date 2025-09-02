// No arquivo CatsVerseApplication.kt (ou o nome que você escolheu)
package com.marin.catsverse // Certifique-se que o pacote está correto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CatsVerseApplication : Application() {
    // Você pode adicionar código de inicialização do aplicativo aqui mais tarde, se necessário.
    // Por exemplo, Timber, bibliotecas de analytics, etc.
    override fun onCreate() {
        super.onCreate()
        // Inicializações podem vir aqui
    }
}