package com.yasunov.shiftapp2.users

import com.yasunov.shiftapp2.database.entity.ShiftEntity

data class User(
    val id: Int,
    val name: String,
    val location: String,
    val phone: String,
    val picture: String,
)

fun ShiftEntity.asUser() = User(
    id = id,
    name = fullName!!,
    location = location!!,
    phone = phone!!,
    picture = imageThumbnail!!
)