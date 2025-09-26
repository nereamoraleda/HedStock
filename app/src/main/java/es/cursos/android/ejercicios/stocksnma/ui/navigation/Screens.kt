package es.cursos.android.ejercicios.stocksnma.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object Home

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object UserCreation

@Serializable
data class UserDetails(val idUser: Long)

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
data class ProductCreation(val barcodeScanner: String?)

@Serializable
data class ProductDetails(val idProduct: String)

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object SupplierCreation

@Serializable
data class SupplierDetails(val idSupplier: String)

// --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---

@Serializable
object Settings
