package es.cursos.android.ejercicios.stocksnma.utils.enums.fields

import es.cursos.android.ejercicios.stocksnma.utils.constants.FieldMaxLengths

enum class StoreFields(val maxLength: Int) {
    NAME(FieldMaxLengths.Common.NAME_MAX),
    EMAIL(FieldMaxLengths.Common.EMAIL_MAX),
    PHONE(FieldMaxLengths.Common.PHONE_MAX),
    ADDRESS(FieldMaxLengths.Common.ADDRESS_MAX),
    CITY(FieldMaxLengths.Common.CITY_MAX),
    COUNTRY(FieldMaxLengths.Common.COUNTRY_MAX),
    ZIP_CODE(FieldMaxLengths.Common.ZIP_CODE_MAX),
    IS_ACTIVE(0)
}