package com.yasunov.shiftapp2.workers


import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yasunov.shiftapp2.database.dao.ShiftDao
import com.yasunov.shiftapp2.database.entity.ShiftEntity
import com.yasunov.shiftapp2.network.NetworkDataSource
import com.yasunov.shiftapp2.network.data.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LoadDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataSource: NetworkDataSource,
    private val notificationManager: NotificationManagerCompat,
    private val notificationCompatBuilder: NotificationCompat.Builder,
    private val dao: ShiftDao
) : CoroutineWorker(
    context,
    workerParams
) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        var users: List<User> = emptyList()
        try {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(
                    1,
                    notificationCompatBuilder
                        .setProgress(100, 20, true).build()
                )
            }

            users = dataSource.getUsers().results
            syncData(users).forEach {
                dao.updateUser(it)
            }
        } catch (e: Exception) {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(
                    2, notificationCompatBuilder
                        .setContentTitle("Ошибка в загрузке")
                        .build()
                )
            }
            return Result.failure()
        }
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(
                3, notificationCompatBuilder
                    .setContentTitle("Данные загружены")
                    .build()
            )
        }

        return Result.success()

    }

    private fun syncData(results: List<User>): List<ShiftEntity> {
        val shiftEntityList = mutableListOf<ShiftEntity>()
        results.forEach {
            val location =
                it.location.country + " " + it.location.city + " " + it.location.street.name + " " + it.location.street.number.toString()
            val geo = it.location.coordinates.longitude + "," + it.location.coordinates.latitude
            val fullname = it.name.first + " " + it.name.last

            shiftEntityList.add(
                ShiftEntity(
                    fullName = fullname,
                    birthday = it.dob.date.split("T")[0],
                    email = it.email,
                    location = location,
                    geo = geo,
                    phone = it.phone,
                    login = it.login.username,
                    password = it.login.password,
                    imageThumbnail = it.picture.thumbnail,
                    image = it.picture.large,
                    id = it.hashCode()
                )
            )
        }
        return shiftEntityList.sortedBy {
            it.location
        }
    }
}