package es.cursos.android.ejercicios.stocksnma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("SELECT * FROM category_table WHERE id = :id")
    fun getCategory(id: Int): Flow<CategoryEntity>

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}