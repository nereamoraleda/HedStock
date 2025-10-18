package es.cursos.android.ejercicios.stocksnma.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.SearchPreferences
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.SessionPreferences
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.SortPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): AppDataStore {
        return AppDataStore(context)
    }


    @Provides @Singleton
    fun provideSessionPreferences(@ApplicationContext context: Context): SessionPreferences =
        SessionPreferences(context)


    @Provides @Singleton
    fun provideSortPreferences(@ApplicationContext context: Context): SortPreferences =
        SortPreferences(context)


    @Provides @Singleton
    fun provideSearchPreferences(@ApplicationContext context: Context): SearchPreferences =
        SearchPreferences(context)
}