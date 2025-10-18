package es.cursos.android.ejercicios.stocksnma.utils.enums

import androidx.annotation.StringRes
import es.cursos.android.ejercicios.stocksnma.R

enum class StoreSections(@StringRes val label: Int) {
    CONTACT(R.string.store_section_contact),
    ADDRESS(R.string.store_section_address)
}