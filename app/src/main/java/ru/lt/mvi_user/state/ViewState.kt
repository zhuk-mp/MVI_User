package ru.lt.mvi_user.state

sealed class  ViewState {
    data class Name(
        val name: String? = null,
        val nameError: String? = null,
        val lastName: String? = null,
        val lastNameError: String? = null,
        val bd: String? = null,
        val bdError: String? = null,
        val not18: Boolean? = null,
        val next: Int? = null,
        val check: Boolean? = null,
            )

    data class Address (
        val country: String? = null,
        val countryError: String? = null,
        val city: String? = null,
        val cityError: String? = null,
        val address: String? = null,
        val addressError: String? = null,
        val next: Int? = null,
    )

    data class Tag (
        val next: Int? = null,
        val selectedTags: MutableList<String>? = null,
    )

    data class Last (
        val name: String? = null,
        val lastName: String? = null,
        val bd: String? = null,
        val fullAddress: String? = null,
        val selectedTags: MutableList<String>? = null,
    )

}