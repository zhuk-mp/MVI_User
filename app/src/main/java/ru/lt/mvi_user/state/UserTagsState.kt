package ru.lt.mvi_user.state

data class UserTagsState (
    val isClickFirst: Boolean = false,
    val next: Int = 0,
    val selectedTags: MutableList<String> = mutableListOf(),
    val check: Boolean = false
)