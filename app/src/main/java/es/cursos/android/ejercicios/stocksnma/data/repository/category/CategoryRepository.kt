package es.cursos.android.ejercicios.stocksnma.data.repository.category

import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)


    fun getCategory(id: Int): Flow<CategoryEntity?>
    fun getAllCategories(): Flow<List<CategoryEntity>>
}