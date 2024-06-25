package com.falikiali.rschapp.di

import com.falikiali.rschapp.data.AuthRepositoryImpl
import com.falikiali.rschapp.data.CartRepositoryImpl
import com.falikiali.rschapp.data.CategoryRepositoryImpl
import com.falikiali.rschapp.data.PreferencesRepositoryImpl
import com.falikiali.rschapp.data.ProductRepositoryImpl
import com.falikiali.rschapp.data.ProfileRepositoryImpl
import com.falikiali.rschapp.domain.repository.AuthRepository
import com.falikiali.rschapp.domain.repository.CartRepository
import com.falikiali.rschapp.domain.repository.CategoryRepository
import com.falikiali.rschapp.domain.repository.PreferencesRepository
import com.falikiali.rschapp.domain.repository.ProductRepository
import com.falikiali.rschapp.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun providePreferencesRepository(preferencesRepositoryImpl: PreferencesRepositoryImpl): PreferencesRepository

    @Binds
    @Singleton
    abstract fun provideProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun provideCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun provideCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

}