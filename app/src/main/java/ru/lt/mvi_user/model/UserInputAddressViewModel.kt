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
import ru.lt.mvi_user.state.UserAddressState
import javax.inject.Inject

@HiltViewModel
class UserInputAddressViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    val support: Support
) : ViewModel(), IUserInputViewModel {

    private val _navigateToNextScreen = Channel<Unit>(Channel.BUFFERED)
    val navigateToNextScreen = _navigateToNextScreen.receiveAsFlow()
    private var isClickFirst = true

    val state: MutableLiveData<UserAddressState> = MutableLiveData(UserAddressState())

    init {
        updateViewState {
            copy(
                country = wizardCache.country,
                city = wizardCache.city,
                address = wizardCache.address,
                check = wizardCache.check
            )
        }
    }

    override fun processIntent(intent: UserInputIntent) {
        when (intent) {
            is UserInputIntent.CountryNameChanged ->
                updateViewState {
                    copy(
                        country = intent.country,
                        countryError = support.isValidFields(intent.country, isClickFirst)
                    )
                }

            is UserInputIntent.CityNameChanged ->
                updateViewState {
                    copy(
                        city = intent.city,
                        cityError = support.isValidFields(intent.city, isClickFirst)
                    )
                }

            is UserInputIntent.AddressChanged ->
                updateViewState {
                    copy(
                        address = intent.address,
                        addressError = support.isValidFields(intent.address, isClickFirst)
                    )
                }

            is UserInputIntent.NextButtonClicked -> {
                val state = state.value!!
                if (isClickFirst) {
                    isClickFirst = false
                    updateViewState {
                        copy(
                            countryError = support.isValidFields(state.country,isClickFirst),
                            cityError = support.isValidFields(state.city,isClickFirst),
                            addressError = support.isValidFields(state.address,isClickFirst)
                        )
                    }
                }
                validateAndSave()
            }

            else -> {}
        }
    }

    fun updateViewState(block: UserAddressState.() -> UserAddressState) {
        val oldState = state.value!!
        val newState = block(oldState)
        state.value = newState
    }

    override fun validateAndSave() {
        val state = state.value
        val isSave = support.validateResult(isValid(state))
        if (isSave[0] == true) {
            wizardCache.country = state!!.country
            wizardCache.city = state.city
            wizardCache.address = state.address
            updateViewState {
                copy(
                    next = if (state.check)
                        R.id.action_userInputFragment2_to_userInputFragment4
                    else
                        R.id.action_userInputFragment2_to_userInputFragment3
                )
            }
            viewModelScope.launch {
                _navigateToNextScreen.send(Unit)
            }
        }
    }

    private fun isValid(state: UserAddressState?): ValidateState? {
        if (state == null)
            return null
        return if (state.countryError != null || state.cityError != null || state.addressError != null)
            ValidateState.LoseFiled
        else
            ValidateState.Ok
    }
}
