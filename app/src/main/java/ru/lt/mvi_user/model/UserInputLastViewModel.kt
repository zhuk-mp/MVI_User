package ru.lt.mvi_user.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.UserInputIntent
import javax.inject.Inject

@HiltViewModel
class UserInputLastViewModel @Inject constructor(
    val support: Support
) : ViewModel(), IUserInputViewModel {
    override fun processIntent(intent: UserInputIntent) {
    }

    override fun validateAndSave() {
    }
}
