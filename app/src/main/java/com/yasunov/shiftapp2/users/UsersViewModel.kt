package com.yasunov.shiftapp2.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasunov.shiftapp2.data.ShiftRepository
import com.yasunov.shiftapp2.database.entity.ShiftEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: ShiftRepository
) : ViewModel() {
    private var _appUiState: MutableStateFlow<UserUiState> = MutableStateFlow(UserUiState.Loading)
    val appUiState: StateFlow<UserUiState> = _appUiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getUsers().flowOn(Dispatchers.IO).collect { list ->
                _appUiState.update {
                    UserUiState.Success(shiftEntityToUser(list))
                }
            }

        }

    }
    fun resetUsers() {
        viewModelScope.launch(Dispatchers.Main) {
            _appUiState.update {
                UserUiState.Loading
            }
            launch(Dispatchers.IO) { repository.resetUsers() }
            var users: List<User> = emptyList()
            viewModelScope.launch(Dispatchers.IO) {
                repository.getUsers().flowOn(Dispatchers.IO).collect {list ->
                    _appUiState.update {
                        UserUiState.Success(
                            users = shiftEntityToUser(list)
                        )
                    }

                }

            }


        }

    }

    private fun shiftEntityToUser(list: List<ShiftEntity>): List<User> {
        val users: MutableList<User> = mutableListOf()
        list.forEach {
            users.add(it.asUser())
        }
        return users
    }
}
