package com.yasunov.shiftapp2.network.di

import com.yasunov.shiftapp2.network.NetworkDataSource
import com.yasunov.shiftapp2.network.retrofit.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    fun bind(retrofitNetwork: RetrofitNetwork): NetworkDataSource
}