package ru.lt.mvi_user.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.UserInputIntent
import ru.lt.mvi_user.state.ValidateState
import ru.lt.mvi_user.data.WizardCache
import ru.lt.mvi_user.state.UserTagsState
import javax.inject.Inject

@HiltViewModel
class UserInputTagsViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    val support: Support
) : ViewModel(), IUserInputViewModel {

    private val _navigateToNextScreen = Channel<Unit>(Channel.BUFFERED)
    val navigateToNextScreen = _navigateToNextScreen.receiveAsFlow()

    val state: MutableLiveData<UserTagsState> = MutableLiveData(UserTagsState())

    init {
        updateViewState {
            copy(
                selectedTags = wizardCache.selectedTags,
                check = wizardCache.check
            )
        }
    }

    override fun processIntent(intent: UserInputIntent) {
        when (intent) {
            is UserInputIntent.TagChanged -> {
                val selectedTags = state.value!!.selectedTags
                if (intent.isSelected)
                    selectedTags.add(intent.tag)
                else
                    selectedTags.remove(intent.tag)
                updateViewState {copy(selectedTags = selectedTags)}
            }
            is UserInputIntent.NextButtonClicked -> {
                validateAndSave()
            }
            else -> {}
        }
    }

     override fun validateAndSave() {
        val state = state.value
        val isSave = support.validateResult(isValid(state))
         if (isSave[0] == true) {
            wizardCache.selectedTags = state!!.selectedTags
            updateViewState {
                copy(next = if (state.check)
                    R.id.action_userInputFragment3_to_userInputFragment2
                else
                    R.id.action_userInputFragment3_to_userInputFragment4,
                    isClickFirst = !state.check)
            }
            viewModelScope.launch {
                _navigateToNextScreen.send(Unit)
            }
        }
    }

    fun updateViewState(block: UserTagsState.() -> UserTagsState) {
        val oldState = state.value!!
        val newState = block(oldState)
        state.value = newState
    }

      private fun isValid(state: UserTagsState?): ValidateState? {
        if (state == null)
            return null
        return if (state.selectedTags.isEmpty()) {
            support.log(support.getString(R.string.selectedtags),true)
            ValidateState.LoseFiled
        }
        else
            ValidateState.Ok
    }
}
