package es.cursos.android.ejercicios.stocksnma.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


/**
 * Función - Formatear precios (0,00€ - 0.00$...)
 */
fun formatedPrice(precio: Double): String {
    return NumberFormat.getCurrencyInstance().format(precio)
}



fun getCurrencySymbol(): String {
    val locale = Locale.getDefault()
    val currency = Currency.getInstance(locale)
    return currency.symbol
}