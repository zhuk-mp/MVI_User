package ru.lt.mvi_user.model

import ru.lt.mvi_user.data.Intent

interface IUserInputViewModel {
    fun processIntent(intent: Intent)
    fun validateAndSave()
}