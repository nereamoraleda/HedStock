package es.cursos.android.ejercicios.stocksnma.data.repository.category

import es.cursos.android.ejercicios.stocksnma.data.local.dao.CategoryDao
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryOfflineRepository @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {
    override suspend fun insertCategory(category: CategoryEntity) = categoryDao.insertCategory(category)
    override suspend fun updateCategory(category: CategoryEntity) = categoryDao.updateCategory(category)
    override suspend fun deleteCategory(category: CategoryEntity) = categoryDao.deleteCategory(category)

    override fun getCategory(id: Int): Flow<CategoryEntity?> = categoryDao.getCategory(id)
    override fun getAllCategories(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
}