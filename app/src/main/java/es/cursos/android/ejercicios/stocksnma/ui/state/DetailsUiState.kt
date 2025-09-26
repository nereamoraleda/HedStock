package es.cursos.android.ejercicios.stocksnma.ui.state


/**
 * SEALED CLASS - Estados de las pantallas "ProductDetails" y "SupplierDetails"
 */
sealed class DetailsUiState<out T> {
    data object Loading: DetailsUiState<Nothing>()
    data class Success<T>(val item: T, val isEntryValid: Boolean): DetailsUiState<T>()
    data object NotFound: DetailsUiState<Nothing>()
    data class Error(val messageError: String): DetailsUiState<Nothing>()
}


data class CreationUiState<T>(
    val item: T,
    val isEntryValid: Boolean = false
)
