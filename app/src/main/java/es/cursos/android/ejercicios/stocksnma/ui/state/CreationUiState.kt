package es.cursos.android.ejercicios.stocksnma.ui.state

// TODO - Modificar nombre una vez que se elimine por completo el otro
data class CreateUiState<T>(
    val newItem: T,
    val isFormValid: Boolean = false
)