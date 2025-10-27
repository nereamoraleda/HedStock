package es.cursos.android.ejercicios.stocksnma.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Home

// -------------------- PRODUCT SCREENS -------------------- //
@Serializable
data class ProductCreation(val barcodeScanner: String?)

@Serializable
data class ProductDetails(val idProduct: String)


// -------------------- SUPPLIER SCREENS -------------------- //
@Serializable
object SupplierCreation

@Serializable
data class SupplierDetails(val idSupplier: String)


// -------------------- USER SCREENS -------------------- //
@Serializable
object UserCreation

@Serializable
data class UserDetails(val idUser: Long)


// -------------------- STORE SCREENS -------------------- //
@Serializable
object StoreCreation

@Serializable
data class StoreDetails(val idStore: Long)


// -------------------- SETTINGS SCREEN -------------------- //
@Serializable
object Settings
