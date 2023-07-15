package ru.lt.mvi_user.state

data class UserDateState (
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val dateOfBirth: String = "",
    val dateOfBirthError: String? = null,
    val not18: Boolean? = null,
    val next: Int = 0,
    val check: Boolean = false
)