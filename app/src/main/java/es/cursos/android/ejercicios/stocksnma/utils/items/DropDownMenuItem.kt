package es.cursos.android.ejercicios.stocksnma.utils.items

import androidx.annotation.StringRes

data class DropDownMenuItem(
    @StringRes val title: Int,
    val selected: Boolean,
    val action: () -> Unit
)
