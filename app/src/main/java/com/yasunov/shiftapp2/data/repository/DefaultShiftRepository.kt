package com.yasunov.shiftapp2.data.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yasunov.shiftapp2.data.ShiftRepository
import com.yasunov.shiftapp2.database.dao.ShiftDao
import com.yasunov.shiftapp2.database.entity.ShiftEntity
import com.yasunov.shiftapp2.workers.LoadDataWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultShiftRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val dao: ShiftDao
) : ShiftRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun resetUsers() {
        CoroutineScope(Dispatchers.IO).launch {
//            OneTimeWorkRequest.Companion.from(LoadDataWorker::class.java)
            val workRequest = OneTimeWorkRequestBuilder<LoadDataWorker>().build()
            workManager.enqueue(workRequest)
            val deleteDataTask = launch(Dispatchers.IO) {
                dao.deleteAllData()
            }
            deleteDataTask.join()
        }
    }

    override suspend fun getUsers(): Flow<List<ShiftEntity>> {
        return dao.getAllUsers()
    }

    override suspend fun getUserById(id: Int): ShiftEntity {
        return dao.getProfileById(id)
    }

}