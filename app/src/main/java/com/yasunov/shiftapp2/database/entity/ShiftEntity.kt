package com.yasunov.shiftapp2.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shift_table")
class ShiftEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = -1,
    @ColumnInfo(name = "full_name")
    val fullName: String? = "",
    @ColumnInfo(name = "birthday")
    val birthday: String? = "",
    @ColumnInfo(name = "email")
    val email: String? = "",
    @ColumnInfo(name = "location")
    val location: String? = "",
    @ColumnInfo(name = "geo")
    val geo: String? = "",
    @ColumnInfo(name = "phone")
    val phone: String? = "",
    @ColumnInfo(name = "login")
    val login: String? = "",
    @ColumnInfo(name = "password")
    val password: String? = "",
    @ColumnInfo(name = "image_thumbnail")
    val imageThumbnail: String? = "",
    @ColumnInfo(name = "image")
    val image: String? = "",
)
