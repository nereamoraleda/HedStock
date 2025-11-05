package es.cursos.android.ejercicios.stocksnma.utils.constants

object FieldMaxLengths {
    object Common {
        const val NAME_MAX = 100
        const val CONTACT_NAME_MAX = 80
        const val EMAIL_MAX = 120
        const val PHONE_MAX = 20
        const val ADDRESS_MAX = 150
        const val CITY_MAX = 60
        const val COUNTRY_MAX = 60
        const val ZIP_CODE_MAX = 15
    }

    object ProductCatalogs {
        const val BRAND_MAX = 60
        const val BARCODE_MAX = 30
    }

    object UserFieldMaxLengths {
        const val USERNAME_MAX = 50
        const val PASSWORD_MIN = 8 // TODO - Añadir más adelante y en otro lado (inicio de sesión más cómodo)
        const val ROLE = 30        // TODO - Añadir más adelante (aún no manejado)
    }
}