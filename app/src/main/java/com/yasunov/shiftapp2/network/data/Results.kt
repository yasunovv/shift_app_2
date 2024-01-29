package com.yasunov.shiftapp2.network.data

data class Results(
    val results: List<User>
)
data class User(
    val name: Name,
    val location: Location,
    val login: Login,
    val phone: String,
    val picture: Picture,
    val email: String,
    val dob: BirthDay,

)
data class Name(
    val first: String,
    val last: String,
)
data class Location(
    val street: Street,
    val city: String,
    val country: String,
    val coordinates: Coordinates,
)

data class Street(
    val number: Int,
    val name: String
)

data class Coordinates(
    val latitude: String,
    val longitude: String
)
data class Login(
    val username: String,
    val password: String,
)

data class Picture(
    val large: String,
    val thumbnail: String
)

data class BirthDay(
    //ISO 8601 format
    val date: String,
)


