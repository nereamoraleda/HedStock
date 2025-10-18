package es.cursos.android.ejercicios.stocksnma.utils.validations

/**
 * DATA CLASS - ProductValidationState
 * Maneja los errores de cada campo del formulario de creación y modificación de productos
 *
 * @param nameErrorMessage Mensaje de error para el campo nombre
 * @param barcodeErrorMessage Mensaje de error para el campo código de barras
 * @param supplierErrorMessage Mensaje de error para el campo proveedor
 * @param categoryErrorMessage Mensaje de error para el campo categoría
 * @param costPriceErrorMessage Mensaje de error para el campo precio de compra
 * @param sellingPriceErrorMessage Mensaje de error para el campo precio de venta
 * @param stockErrorMessage Mensaje de error para el campo cantidad en stock
 * @param minStockErrorMessage Mensaje de error para el campo cantidad mínima en stock
 * @param maxStockErrorMessage Mensaje de error para el campo cantidad máxima en stock
 *
 */
data class ProductValidationState(
    val nameErrorMessage: String? = null,
    val barcodeErrorMessage: String? = null,
    val supplierErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,
    val costPriceErrorMessage: String? = null,
    val sellingPriceErrorMessage: String? = null,
    val stockErrorMessage: String? = null,
    val minStockErrorMessage: String? = null,
    val maxStockErrorMessage: String? = null
)

data class SupplierValidationState(
    val nameErrorMessage: String? = null,
    val phoneErrorMessage: String? = null,
    val emailErrorMessage: String? = null
)


data class UserValidationState(
    val nameErrorMessage: String? = null,
    val usernameErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val phoneErrorMessage: String? = null,
    val contactInformationErrorMessage: String? = null,
    val roleErrorMessage: String? = null,
)