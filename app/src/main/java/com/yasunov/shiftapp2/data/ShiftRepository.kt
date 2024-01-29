package com.yasunov.shiftapp2.data

import com.yasunov.shiftapp2.database.entity.ShiftEntity
import kotlinx.coroutines.flow.Flow

interface ShiftRepository {
    fun resetUsers()
    suspend fun getUsers(): Flow<List<ShiftEntity>>
    suspend fun getUserById(id: Int): ShiftEntity

}