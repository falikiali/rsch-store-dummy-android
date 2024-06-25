package com.falikiali.rschapp.di

import com.falikiali.rschapp.BuildConfig
import com.falikiali.rschapp.data.remote.AuthApiService
import com.falikiali.rschapp.data.remote.AuthInterceptor
import com.falikiali.rschapp.data.remote.CartApiService
import com.falikiali.rschapp.data.remote.CategoryApiService
import com.falikiali.rschapp.data.remote.ProductApiService
import com.falikiali.rschapp.data.remote.ProfileApiService
import com.falikiali.rschapp.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideAuthInterceptor(preferencesRepository: PreferencesRepository): AuthInterceptor {
        return AuthInterceptor(preferencesRepository)
    }

    @Singleton
    @Provides
    fun provideAuthApiService(): AuthApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3000/api/v3/authentication/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApiService(authInterceptor: AuthInterceptor): ProfileApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3001/api/v3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ProfileApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoryApiService(): CategoryApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3002/api/v3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CategoryApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideProductApiService(): ProductApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3003/api/v3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ProductApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideCartApiService(authInterceptor: AuthInterceptor): CartApiService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:3004/api/v3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CartApiService::class.java)
    }

}