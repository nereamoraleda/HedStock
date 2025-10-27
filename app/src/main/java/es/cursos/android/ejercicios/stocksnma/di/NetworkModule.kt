package es.cursos.android.ejercicios.stocksnma.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.cursos.android.ejercicios.stocksnma.data.remote.api.AuthApi
import es.cursos.android.ejercicios.stocksnma.data.remote.api.HedstockApiService
import es.cursos.android.ejercicios.stocksnma.data.remote.api.UserApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8081" // localhost del emulador Android
                                                        // No se conecta si el emulador no tiene conexión a internet

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    // -------------------- APIs --------------------
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): HedstockApiService {
        return retrofit.create(HedstockApiService::class.java)
    }
}