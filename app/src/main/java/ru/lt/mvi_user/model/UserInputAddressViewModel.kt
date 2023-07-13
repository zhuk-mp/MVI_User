package ru.lt.mvi_user.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.UserInputIntent
import ru.lt.mvi_user.data.ValidateState
import ru.lt.mvi_user.data.ViewState
import ru.lt.mvi_user.data.WizardCache
import javax.inject.Inject

@HiltViewModel
class UserInputAddressViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    val support: Support
) : ViewModel(), IUserInputViewModel{

    private val _navigateToNextScreen = Channel<Unit>(Channel.BUFFERED)
    val navigateToNextScreen = _navigateToNextScreen.receiveAsFlow()

    override fun processIntent(intent: UserInputIntent) {
        when (intent) {
            is UserInputIntent.CountryNameChanged -> support.updateViewState {copy(country = intent.country, countryError = support.isValidFields(intent.country))}
            is UserInputIntent.CityNameChanged -> support.updateViewState {copy(city = intent.city, cityError = support.isValidFields(intent.city))}
            is UserInputIntent.AddressChanged -> support.updateViewState {copy(address = intent.address, addressError = support.isValidFields(intent.address))}
            is UserInputIntent.NextButtonClicked -> {
                val state = support.state.value!!
                if (!state.isClickFirst) {
                    support.updateViewState {copy(isClickFirst = true)}
                    support.updateViewState {
                        copy(
                            countryError = support.isValidFields(state.country),
                            cityError = support.isValidFields(state.city),
                            addressError = support.isValidFields(state.address)
                        )
                    }
                }
                validateAndSave()
            }
            else -> {}
        }
    }

    override fun validateAndSave() {
        val state = support.state.value
        val isSave = support.validateResult(isValid(state))
        if (isSave) {
            wizardCache.country = state!!.country
            wizardCache.city = state.city
            wizardCache.address = state.address
            support.updateViewState {
                copy(next = if (state.check)
                    R.id.action_userInputFragment2_to_userInputFragment4
                else
                    R.id.action_userInputFragment2_to_userInputFragment3)
            }
            viewModelScope.launch {
                _navigateToNextScreen.send(Unit)
            }
        }
    }

     override fun isValid(state: ViewState?): ValidateState? {
        if (state == null)
            return null
        return if (state.countryError != null || state.cityError != null || state.addressError != null)
            ValidateState.LoseFiled
        else
            ValidateState.Ok
    }
}
