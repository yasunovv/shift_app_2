package com.yasunov.shiftapp2.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yasunov.shiftapp2.database.entity.ShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(entity: ShiftEntity)
    @Query("SELECT `id`, `full_name`,`location`,`geo`,`phone`,`image`,`birthday`,`login`,`password`,`email` FROM `shift_table` WHERE `id` = :id LIMIT 1")
    suspend fun getProfileById(id: Int): ShiftEntity
    @Query("DELETE FROM `shift_table`")
    suspend fun deleteAllData()
    @Query("SELECT count(id) FROM `shift_table`")
    suspend fun getCountUsers(): Int
    @Query("SELECT `id`, `full_name`, `location`, `phone`, `image_thumbnail` FROM `shift_table` ORDER BY `full_name`")
    fun getAllUsers(): Flow<List<ShiftEntity>>
}
