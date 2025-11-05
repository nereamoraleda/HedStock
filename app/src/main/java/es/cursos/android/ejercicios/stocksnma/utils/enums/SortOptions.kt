package es.cursos.android.ejercicios.stocksnma.utils.enums

enum class ProductSortOptions {
    NAME_ASC,
    NAME_DESC,
    CATEGORY
}


enum class SupplierSortOptions(val sortBy: String?, val direction: String?) {
    NAME_ASC("name", "asc"),
    NAME_DESC("name", "desc"),

    CONT_NAME_ASC("contactName", "asc"),
    CONT_NAME_DESC("contactName", "desc"),

    DATE_OLDEST("createdAt", "asc"),
    DATE_NEWEST("createdAt", "desc")
}


enum class StoreSortOptions(val sortBy: String?, val direction: String?) {
    NAME_ASC("name", "asc"),
    NAME_DESC("name", "desc"),
    DATE_OLDEST("createdAt", "asc"),
    DATE_NEWEST("createdAt", "desc")
}


enum class UserSortOptions(val sortBy: String?, val direction: String?) {
    NAME_ASC("name", "asc"),
    NAME_DESC("name", "desc"),
    DATE_OLDEST("createdAt", "asc"),
    DATE_NEWEST("createdAt", "desc")
}


enum class UserGroupOptions {
    ROLE,
    STORE
}


data class UserFilter(
    val sortOption: UserSortOptions,
    val groupOption: UserGroupOptions? = null,
    val activeFilter: ActiveFilters,
    val refresh: Long = System.currentTimeMillis()
)
