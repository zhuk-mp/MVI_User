package ru.lt.mvi_user.data


data class WizardData (
    val name: String? = null,
    val lastName: String? = null,
    val bd: String? = null,
    val country: String? = null,
    val city: String? = null,
    val address: String? = null,
    val selectedTags: MutableList<String>? = null,
    val check: Boolean? = null,
)
