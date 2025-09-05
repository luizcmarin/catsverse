// =============================================================================
// Arquivo: com.marin.catsverse.ui.Icones.kt
// Descrição: Objeto que centraliza os ícones do aplicativo para fácil referência.
// =============================================================================
package com.marin.catsverse.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CreditCard

object Icones {
    val Boleto = Icons.AutoMirrored.Outlined.ReceiptLong
    val CartaoCredito = Icons.Filled.CreditCard
    val Carteira = Icons.Filled.AccountBalanceWallet
    val Categoria = Icons.Filled.Category
    val Check = Icons.Filled.Check
    val Dinheiro = Icons.Filled.LocalAtm
    val FormaPagamento = Icons.Outlined.CreditCard
    val Inicio = Icons.Default.Home
    val Lixeira = Icons.Default.Delete
    val Menu = Icons.Default.Menu
    val Privacidade = Icons.Default.Policy
    val QrCode = Icons.Filled.QrCode
    val Sobre = Icons.Default.Info
    val Tarefas = Icons.Default.Task
    val Transferencia = Icons.Filled.CurrencyExchange
    val TresPontosVertical = Icons.Filled.MoreVert
    val Voltar = Icons.AutoMirrored.Filled.ArrowBack
}
