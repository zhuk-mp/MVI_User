package ru.lt.mvi_user.data

sealed class ValidateState {
    object Ok : ValidateState()
    object Not18 : ValidateState()
    object LoseFiled : ValidateState()
}