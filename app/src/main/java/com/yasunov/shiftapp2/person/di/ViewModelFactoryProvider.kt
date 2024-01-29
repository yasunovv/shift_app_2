package com.yasunov.shiftapp2.person.di

import com.yasunov.shiftapp2.person.PersonViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun personViewModelFactory(): PersonViewModel.Factory
}