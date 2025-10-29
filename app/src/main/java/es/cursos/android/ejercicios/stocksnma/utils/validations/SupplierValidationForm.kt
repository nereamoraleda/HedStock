package es.cursos.android.ejercicios.stocksnma.utils.validations

data class SupplierValidationForm(
    val nameErrorMessage: String? = null,
    val phoneErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val contactInformationErrorMessage: String? = null,
    val cityErrorMessage: String? = null,
    val countryErrorMessage: String? = null
)