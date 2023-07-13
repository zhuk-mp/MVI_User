package ru.lt.mvi_user.data


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WizardCache @Inject constructor() {
    var firstName: String? = null
    var lastName: String? = null
    var dateOfBirth: String? = null
    var country: String? = null
    var city: String? = null
    var address: String? = null
    var selectedTags: MutableList<String> = mutableListOf()
}