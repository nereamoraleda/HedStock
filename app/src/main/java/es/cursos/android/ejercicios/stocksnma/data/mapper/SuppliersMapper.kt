package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier


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
    address = address ?: "",
    city = "",
    country = "",
    zipCode = ""
)