package es.cursos.android.ejercicios.stocksnma.utils.items

sealed class NavDrawerItem {
    data object Divider : NavDrawerItem()
    data object Header : NavDrawerItem()
    data class Item(
        val title: Int,
        val iconSelected: Int,
        val iconUnselected: Int,
        val count: Int? = null,
        val action: () -> Unit
    ) : NavDrawerItem()
}
