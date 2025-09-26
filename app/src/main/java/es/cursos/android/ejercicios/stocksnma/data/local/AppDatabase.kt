package es.cursos.android.ejercicios.stocksnma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.cursos.android.ejercicios.stocksnma.data.local.dao.CategoryDao
import es.cursos.android.ejercicios.stocksnma.data.local.dao.ProductDao
import es.cursos.android.ejercicios.stocksnma.data.local.dao.SupplierDao
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity

@Database(
    entities = [ProductEntity::class, SupplierEntity::class, CategoryEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
    abstract fun categoryDao(): CategoryDao


    companion object {
        private const val DATABASE_NAME = "inventory_database_v1"

        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    //.addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 1. Renombrar tabla antigua si es necesario
        // 2. Crear tabla nueva con los cambios
        // 3. Copiar datos
        // 4. Eliminar tabla antigua
        // 5. Renombrar nueva tabla al nombre original

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS product_table_new (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                brand TEXT,
                description TEXT,
                barcode TEXT,
                image TEXT,
                cost_price REAL NOT NULL,
                price REAL NOT NULL,
                stock INTEGER NOT NULL,
                min_stock INTEGER NOT NULL,
                max_stock INTEGER,
                id_supplier TEXT,
                category TEXT,
                is_active INTEGER NOT NULL,
                FOREIGN KEY(id_supplier) REFERENCES supplier_table(id) ON DELETE SET NULL
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO product_table_new (
                id, name, brand, description, barcode, image,
                cost_price, price, stock, min_stock, max_stock,
                id_supplier, category, is_active
            )
            SELECT 
                id, name, brand, description, barcode, image,
                cost_price, price, stock, min_stock, max_stock,
                id_supplier, category, is_active
            FROM product_table
        """.trimIndent()
        )

        db.execSQL("DROP TABLE product_table")
        db.execSQL("ALTER TABLE product_table_new RENAME TO product_table")
    }
}
