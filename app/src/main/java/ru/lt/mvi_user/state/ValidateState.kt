package ru.lt.mvi_user.state

sealed class ValidateState {
    object Ok : ValidateState()
    object Not18 : ValidateState()
    object LoseFiled : ValidateState()
}