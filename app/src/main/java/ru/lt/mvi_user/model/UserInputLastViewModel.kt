package ru.lt.mvi_user.model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.data.Support
import javax.inject.Inject

@HiltViewModel
class UserInputLastViewModel @Inject constructor(
//    private val wizardCache: WizardCache,
    val support: Support
) : ViewModel()
//    , IUserInputViewModel
{
//
//    val state: MutableLiveData<UserLastState> = MutableLiveData(UserLastState())
//
//    init {
//        updateViewState {
//            copy(
//                firstName = wizardCache.firstName,
//                lastName = wizardCache.firstName,
//                dateOfBirth = wizardCache.firstName,
//                country = wizardCache.country,
//                city = wizardCache.city,
//                address = wizardCache.address,
//                selectedTags = wizardCache.selectedTags
//            )
//        }
//    }
//
//    fun updateViewState(block: UserLastState.() -> UserLastState) {
//        val oldState = state.value!!
//        val newState = block(oldState)
//        state.value = newState
//    }
//
//
//    override fun processIntent(intent: Intent) {
//    }
//
//    override fun validateAndSave() {
//    }
}
