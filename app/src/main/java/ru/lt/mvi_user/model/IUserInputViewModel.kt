package ru.lt.mvi_user.model

import ru.lt.mvi_user.data.UserInputIntent

interface IUserInputViewModel {
    fun processIntent(intent: UserInputIntent)
    fun validateAndSave()
}