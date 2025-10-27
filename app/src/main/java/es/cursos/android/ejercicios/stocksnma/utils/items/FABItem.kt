package es.cursos.android.ejercicios.stocksnma.utils.items

data class FABItem(
    val title: Int,
    val icon: Int,
    val action: () -> Unit
)
