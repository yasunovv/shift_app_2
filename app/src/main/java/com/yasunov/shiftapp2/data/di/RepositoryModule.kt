package com.yasunov.shiftapp2.data.di

import com.yasunov.shiftapp2.data.ShiftRepository
import com.yasunov.shiftapp2.data.repository.DefaultShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideRepository(impl: DefaultShiftRepository): ShiftRepository
}