package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import es.cursos.android.ejercicios.stocksnma.utils.Constants

/**
 * Entity Category - Tabla que representa a una categoría de productos
 *
 * @property id Identificador único de la categoría (autogenerado)
 * @property name Nombre de la categoría
 */
@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String
)