package com.yasunov.shiftapp2.person

interface PersonUiState {
    object Loading : PersonUiState
    object Error : PersonUiState
    data class Success(
        val person: PersonProfile,
    ) : PersonUiState
}