package com.yasunov.shiftapp2.person

import com.yasunov.shiftapp2.database.entity.ShiftEntity

data class PersonProfile(
    val id: Int,
    val name: String,
    val location: String,
    val geo: String,
    val phone: String,
    val picture: String,
    val birthday: String,
    val login: String,
    val password: String,
    val email: String,
)

fun ShiftEntity.asPersonProfile() = PersonProfile(
    id = id,
    name = fullName!!,
    location = location!!,
    geo = geo!!,
    phone = phone!!,
    picture = image!!,
    birthday = birthday!!,
    login = login!!,
    password = password!!,
    email = email!!

)
