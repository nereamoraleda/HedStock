package es.cursos.android.ejercicios.stocksnma.utils.enums

enum class UserRoles() {
    ADMIN,
    GERENTE,
    VENDEDOR,
    DESCONOCIDO;

    companion object {
        fun getRoles(): List<UserRoles> = entries.toList()
    }
}

//enum class UserRole(val role: String) {
//    ADMIN("Administrador"),
//    EDITOR("Editor"),
//    VIEWER("Solo lectura");
//
//    companion object {
//        fun getRoles(): List<UserRole> = entries.toList()
//    }
//}
