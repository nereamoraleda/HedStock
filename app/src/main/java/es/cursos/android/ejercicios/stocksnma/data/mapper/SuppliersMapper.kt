package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierDto
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier

fun SupplierDto.toSupplier(): Supplier = Supplier(
    id = id.toString(), // TODO - Hasta eliminar todo SupplierEntity mantener Supplier con id como String
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


fun Supplier.toSupplierEntity(): SupplierEntity = SupplierEntity(
    id = id,
    name = name,
    contactName = contactName,
    email = email,
    phone = phone,
    address = address,
)


fun SupplierEntity.toSupplier(): Supplier = Supplier(
    id = id,
    name = name,
    contactName = contactName ?: "",
    email = email ?: "",
    phone = phone ?: "",
    address = address ?: ""
//    city = "",
//    country = "",
//    zipCode = ""
)