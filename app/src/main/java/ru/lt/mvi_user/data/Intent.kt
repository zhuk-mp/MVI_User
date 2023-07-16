package ru.lt.mvi_user.data

import java.util.Date

sealed class Intent {
    data class NameEntered(val name: String) : Intent()
    data class LastNameEntered(val lastName: String) : Intent()
    data class BdEntered(val date: Date) : Intent()
    data class CountryEntered(val country: String) : Intent()
    data class CityEntered(val city: String) : Intent()
    data class AddressEntered(val address: String) : Intent()
    data class SwitchEntered(val check: Boolean) : Intent()
    data class TagEntered(val tag: String, val isSelected: Boolean) : Intent()
}