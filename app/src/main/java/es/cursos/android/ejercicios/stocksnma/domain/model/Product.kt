package es.cursos.android.ejercicios.stocksnma.domain.model

import java.util.UUID


/**
 * DATA CLASS - Product
 * Clase intermedia con ProductEntity, la BD y la UI (conversiones)
 *
 * @param id Identificador único del producto
 * @param name Nombre del producto
 * @param brand Marca del producto
 * @param description Descripción del producto
 * @param barcode Código de barras del producto
 * @param costPrice Precio de compra del producto
 * @param price Precio de venta del producto
 * @param stock Cantidad en stock del producto
 * @param minStock Cantidad mínima en stock del producto
 * @param maxStock Cantidad máxima en stock del producto
 * @param supplierId Identificador del proveedor del producto
 * @param category Categoría del producto
 * @param image Imagen del producto
 * @param isActive Indica si el producto está activo o inactivo
 *
 */
data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val brand: String = "",
    val description: String = "",
    val barcode: String = "",
    val costPrice: String = "",
    val price: String = "",
    val stock: String = "",
    val minStock: String = "",
    val maxStock: String = "",
    val supplierId: String = "",
    val category: String = "",
    val image: String = "",
    val isActive: Boolean = true
)
