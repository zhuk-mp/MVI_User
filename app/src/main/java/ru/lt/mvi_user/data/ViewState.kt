package ru.lt.mvi_user.data

data class ViewState (
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val dateOfBirth: String = "",
    val dateOfBirthError: String? = null,
    val country: String = "",
    val countryError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val not18: Boolean? = null,
    val isClickFirst: Boolean = false,
    val next: Int = 0,
    val selectedTags: MutableList<String> = mutableListOf(),
    val check: Boolean = false
)