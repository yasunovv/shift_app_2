package com.yasunov.shiftapp2.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yasunov.shiftapp2.data.ShiftRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonViewModel @AssistedInject constructor(
    private val repository: ShiftRepository,
    @Assisted private val id: Int,
) : ViewModel() {
    private var _appUiState: MutableStateFlow<PersonUiState> = MutableStateFlow(PersonUiState.Loading)
    val appUiState: StateFlow<PersonUiState> = _appUiState.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {

            val person =  repository.getUserById(id).asPersonProfile()
            launch(Dispatchers.Main) {

            }
            _appUiState.update {
                PersonUiState.Success(
                    person
                )
            }

        }
    }

    override fun onCleared() {
        _appUiState.update {
            PersonUiState.Loading
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            id: Int,
        ): PersonViewModel
    }
    companion object {
        fun provideMainViewModelFactory(factory: Factory, id: Int) : ViewModelProvider.Factory {
            return object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(id) as T
                }
            }
        }
    }

}