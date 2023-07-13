package ru.lt.mvi_user.model

import ru.lt.mvi_user.data.UserInputIntent
import ru.lt.mvi_user.data.ValidateState
import ru.lt.mvi_user.data.ViewState

interface IUserInputViewModel {
    fun processIntent(intent: UserInputIntent)
    fun validateAndSave()
    fun isValid(state: ViewState?): ValidateState? {
        return null
    }
}