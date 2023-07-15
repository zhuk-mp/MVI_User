package ru.lt.mvi_user.state

data class UserAddressState (
    val country: String = "",
    val countryError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val next: Int = 0,
    val check: Boolean = false
)