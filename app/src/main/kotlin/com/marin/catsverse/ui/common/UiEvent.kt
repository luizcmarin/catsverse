package com.marin.catsverse.ui.common

import androidx.annotation.StringRes

/**
 * Eventos de UI que podem ser enviados do ViewModel para a Screen.
 */
sealed class UiEvent {
    data class ShowSnackbar(@StringRes val messageResId: Int, val args: List<Any> = emptyList()) : UiEvent()
    data class ShowToast(@StringRes val messageResId: Int, val args: List<Any> = emptyList()) : UiEvent()
    object NavigateUp : UiEvent()
    // Adicione outros eventos comuns, como Navigate(route: String), etc.
}