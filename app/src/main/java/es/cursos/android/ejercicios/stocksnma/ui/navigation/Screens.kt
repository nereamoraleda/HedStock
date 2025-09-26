package es.cursos.android.ejercicios.stocksnma.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object ProductCreation

@Serializable
data class ProductDetails(val idProduct: String)

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object SupplierCreation

@Serializable
data class SupplierDetails(val idSupplier: String)

