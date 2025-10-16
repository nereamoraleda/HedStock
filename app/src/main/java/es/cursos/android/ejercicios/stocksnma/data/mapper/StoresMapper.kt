package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreGeneralViewDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreSelectionDto
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.StoreGeneralView
import es.cursos.android.ejercicios.stocksnma.domain.model.StoreSelection

fun StoreDto.toStore(): Store = Store(
    id = id,
    name = name,
    email = email ?: "",
    phone = phone ?: "",
    address = address ?: "",
    city = city ?: "",
    country = country ?: "",
    postalCode = postalCode ?: "",
    isActive = isActive ?: true,
    createdAt = createdAt
)


fun Store.toStoreDto(): StoreDto = StoreDto(
    id = id,
    name = name,
    email = email,
    phone = phone,
    address = address,
    city = city,
    country = country,
    postalCode = postalCode,
    isActive = isActive,
    createdAt = createdAt
)


fun StoreGeneralViewDto.toStoreGeneralView(): StoreGeneralView = StoreGeneralView(
    id = id,
    name = name,
    email = email ?: "Email no disponible",
    city = city ?: "Ciudad no disponible"
)


fun StoreSelectionDto.toStoreSelection(): StoreSelection = StoreSelection(
    id = id,
    name = name
    //isActive = isActive
)