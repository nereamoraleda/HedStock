package es.cursos.android.ejercicios.stocksnma.utils.validations

data class StoreValidationForm(
    val nameMessageError: String? = null,
    val emailMessageError: String? = null,
    val phoneMessageError: String? = null,
    val contactInformationErrorMessage: String? = null,
    val addressErrorMessage: String? = null,
    val cityErrorMessage: String? = null,
    val countryErrorMessage: String? = null,
    val postalCodeErrorMessage: String? = null,
)
