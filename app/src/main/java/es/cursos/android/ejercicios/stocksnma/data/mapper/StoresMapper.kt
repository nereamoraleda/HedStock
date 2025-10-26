package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreGeneralViewDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreSelectionDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreRequestDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreResponseDto
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.store.StoreGeneralView
import es.cursos.android.ejercicios.stocksnma.domain.model.store.StoreSelection

// StoreResponseDto (Backend) -> Store (Ui) - Obtener datos de una tienda (No hace falta al revés)
fun StoreResponseDto.toStore(): Store = Store(
    id = id,
    name = name,
    email = email ?: "",
    phone = phone ?: "",
    address = address ?: "",
    city = city ?: "",
    country = country ?: "",
    postalCode = postalCode ?: "",
    isActive = isActive
    //createdAt = createdAt
)


// Store (Ui) -> StoreRequest (Backend) - Para crear/modificar tienda
fun Store.toStoreRequest(): StoreRequestDto = StoreRequestDto(
    name = name,
    email = email.ifBlank { null },
    phone = phone.ifBlank { null },
    address = address.ifBlank { null },
    city = city.ifBlank { null },
    country = country.ifBlank { null },
    postalCode = postalCode.ifBlank { null },
    isActive = isActive
)


// StoreGeneralViewDto (Backend) -> StoreGeneralView (Ui) - No hace falta al revés
fun StoreGeneralViewDto.toStoreGeneralView(): StoreGeneralView = StoreGeneralView(
    id = id,
    name = name,
    email = email,
    city = city,
    isActive = isActive
)


// StoreSelectionDto (Backend) -> StoreSelection (Ui) - No hace falta al revés
fun StoreSelectionDto.toStoreSelection(): StoreSelection = StoreSelection(
    id = id,
    name = name
)

fun String?.orDefault(default: String) = this ?: default