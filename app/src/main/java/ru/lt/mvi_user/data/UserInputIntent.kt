package ru.lt.mvi_user.data

sealed class UserInputIntent {
    data class FirstNameChanged(val firstName: String) : UserInputIntent()
    data class LastNameChanged(val lastName: String) : UserInputIntent()
    data class BateOfBirthChanged(val dateOfBirth: String) : UserInputIntent()
    data class CountryNameChanged(val country: String) : UserInputIntent()
    data class CityNameChanged(val city: String) : UserInputIntent()
    data class AddressChanged(val address: String) : UserInputIntent()
    data class SwitchFragmentsChanged(val check: Boolean) : UserInputIntent()
    data class TagChanged(val tag: String, val isSelected: Boolean) : UserInputIntent()
    object NextButtonClicked : UserInputIntent()
}