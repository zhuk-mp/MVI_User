package ru.lt.mvi_user.data

import java.util.Date


data class WizardData(
    val name: String? = null,
    val lastName: String? = null,
    val bd: Date? = null,
    val country: String? = null,
    val city: String? = null,
    val address: String? = null,
    val selectedTags: MutableList<String> = mutableListOf(),
    val check: Boolean = false,
)
