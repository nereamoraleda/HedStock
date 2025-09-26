package es.cursos.android.ejercicios.stocksnma.data.mapper

import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.domain.model.Category
import es.cursos.android.ejercicios.stocksnma.domain.model.Product
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier


/**
 * FUNCIÓN - Convertir Product a ProductEntity
 * @return ProductEntity
 */
fun Product.toProduct(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    brand = brand,
    description = description,
    barcode = barcode,
    costPrice = costPrice.toDoubleOrNull() ?: 0.0,
    price = price.toDoubleOrNull() ?: 0.0,
    stock = stock.toIntOrNull() ?: 0,
    minStock = minStock.toIntOrNull() ?: 0,
    maxStock = maxStock.toIntOrNull() ?: 0,
    supplierId = if (supplierId.isBlank()) null else (supplierId),
    categoryId = if (category.isBlank()) null else (category.toIntOrNull()),
    image = image,
    isActive = isActive
)


/**
 * FUNCIÓN - Convertir Supplier a SupplierEntity
 * @return SupplierEntity
 */
fun Supplier.toSupplier(): SupplierEntity = SupplierEntity(
    id = id,
    name = name,
    contactName = contactName,
    phone = phone,
    email = email,
    address = "$address, $zipCode \n $city, $country"
)


/**
 * FUNCIÓN - Convertir Category a CategoryEntity
 * @return CategoryEntity
 */
fun Category.toCategory(): CategoryEntity = CategoryEntity(
    id = id,
    name = name
)