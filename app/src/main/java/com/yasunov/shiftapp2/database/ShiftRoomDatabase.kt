package com.yasunov.shiftapp2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yasunov.shiftapp2.database.dao.ShiftDao
import com.yasunov.shiftapp2.database.entity.ShiftEntity

@Database(entities = [ShiftEntity::class], version = 1, exportSchema = false)
abstract class ShiftRoomDatabase : RoomDatabase() {
    abstract fun shiftDao(): ShiftDao
}