package es.cursos.android.ejercicios.stocksnma.utils

/**
 * DATA CLASS - ProductValidationState
 * Maneja los errores de cada campo del formulario de creación y modificación de productos
 *
 * @param nameError Mensaje de error para el campo nombre
 * @param barcodeError Mensaje de error para el campo código de barras
 * @param supplierError Mensaje de error para el campo proveedor
 * @param categoryError Mensaje de error para el campo categoría
 * @param costPriceError Mensaje de error para el campo precio de compra
 * @param priceError Mensaje de error para el campo precio de venta
 * @param stockError Mensaje de error para el campo cantidad en stock
 * @param minStockError Mensaje de error para el campo cantidad mínima en stock
 * @param maxStockError Mensaje de error para el campo cantidad máxima en stock
 *
 */
data class ProductValidationState(
    val nameError: String? = null,
    val barcodeError: String? = null,
    val supplierError: String? = null,
    val categoryError: String? = null,
    val costPriceError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    val minStockError: String? = null,
    val maxStockError: String? = null
)

data class SupplierValidationState(
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null
)


data class UserValidationState(
    val nameError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val emailOrPhoneError: String? = null,
    val roleError: String? = null,
)