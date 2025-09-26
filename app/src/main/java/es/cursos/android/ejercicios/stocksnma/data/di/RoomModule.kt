package es.cursos.android.ejercicios.stocksnma.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.cursos.android.ejercicios.stocksnma.data.local.AppDatabase
import es.cursos.android.ejercicios.stocksnma.data.local.dao.CategoryDao
import es.cursos.android.ejercicios.stocksnma.data.local.dao.ProductDao
import es.cursos.android.ejercicios.stocksnma.data.local.dao.SupplierDao
import es.cursos.android.ejercicios.stocksnma.data.repository.category.CategoryOfflineRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.category.CategoryRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.product.OfflineProductRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.OfflineSupplierRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideSupplierDao(database: AppDatabase): SupplierDao {
        return database.supplierDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return OfflineProductRepository(productDao)
    }

    @Provides
    @Singleton
    fun provideSupplierRepository(supplierDao: SupplierDao): SupplierRepository {
        return OfflineSupplierRepository(supplierDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryOfflineRepository(categoryDao)
    }
}