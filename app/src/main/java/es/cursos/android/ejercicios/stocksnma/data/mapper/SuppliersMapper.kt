package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierHomeViewDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierSelectionMenuDto
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.domain.model.SupplierHomeView
import es.cursos.android.ejercicios.stocksnma.domain.model.SupplierSelectionMenu

// SupplierDto -> Supplier
fun SupplierDto.toSupplier(): Supplier = Supplier(
    id = id,
    name = name,
    contactName = contactName ?: "",
    email = email ?: "",
    phone = phone ?: "",
    address = address ?: "",
    city = city,
    country = country,
    zipCode = zipCode ?: "",
    isActive = isActive
)

// Supplier -> SupplierDto
fun Supplier.toSupplierDto(): SupplierDto = SupplierDto(
    id = id,
    name = name,
    contactName = contactName.ifBlank { null },
    email = email.ifBlank { null },
    phone = phone.ifBlank { null },
    address = address.ifBlank { null },
    city = city,
    country = country,
    zipCode = zipCode.ifBlank { null },
    isActive = isActive
)



// SupplierHomeViewDto -> SupplierHomeView
fun SupplierHomeViewDto.toSupplierHomeView(): SupplierHomeView = SupplierHomeView(
    id = id,
    name = name,
    contactName = contactName ?: "",
    email = email ?: "",
    phone = phone ?: ""
    //isActive = isActive
)

// SupplierSelectionMenuDto -> SupplierSelectionMenu
fun SupplierSelectionMenuDto.toSupplierSelectionMenu(): SupplierSelectionMenu = SupplierSelectionMenu(
    id = id,
    name = name
)



fun Supplier.toSupplierEntity(): SupplierEntity = SupplierEntity(
    id = id.toString(),
    name = name,
    contactName = contactName,
    email = email,
    phone = phone,
    address = address,
)
