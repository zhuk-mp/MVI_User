package ru.lt.mvi_user.data


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WizardCache @Inject constructor() {
    var firstName = ""
    var lastName = ""
    var dateOfBirth = ""
    var country = ""
    var city = ""
    var address = ""
    var selectedTags: MutableList<String> = mutableListOf()
    var check: Boolean = false
}