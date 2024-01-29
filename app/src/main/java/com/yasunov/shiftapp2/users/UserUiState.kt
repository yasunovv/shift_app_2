package com.yasunov.shiftapp2.users

sealed interface UserUiState {
    data class Success(
        val users: List<User>
    ): UserUiState
    data object Loading: UserUiState
    data object Error: UserUiState
}
