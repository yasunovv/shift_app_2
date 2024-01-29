package com.yasunov.shiftapp2.database.di

import android.content.Context
import androidx.room.Room
import com.yasunov.shiftapp2.database.ShiftRoomDatabase
import com.yasunov.shiftapp2.database.dao.ShiftDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {
    @Provides
    fun provideLogDao(database: ShiftRoomDatabase): ShiftDao {
        return database.shiftDao()
    }
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): ShiftRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShiftRoomDatabase::class.java,
            "shift_database"
        ).build()
    }
}