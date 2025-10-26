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


object StoreFieldsLenghts {
    const val STORE_NAME_MAX = 100
    const val STORE_EMAIL_MAX = 100
    const val STORE_PHONE_MAX = 20
    const val STORE_CITY_MAX = 100
    const val STORE_COUNTRY_MAX = 100
    const val STORE_CODE_MAX = 20
}
