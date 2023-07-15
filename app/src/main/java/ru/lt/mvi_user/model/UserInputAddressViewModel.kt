package ru.lt.mvi_user.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.data.Intent
import ru.lt.mvi_user.data.SingletonWizardData
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.WizardData
import ru.lt.mvi_user.state.ViewState
import javax.inject.Inject

@HiltViewModel
class UserInputAddressViewModel @Inject constructor(
    singletonWizardData: SingletonWizardData,
    val support: Support
) : ViewModel()
{
    var data = singletonWizardData.wizardData

    val viewState: MutableLiveData<ViewState.Address> = MutableLiveData()

    fun onCountryEntered(country: String) {
        data = data.processNameChange(Intent.CountryEntered(country))
        viewState.value = data.renderAddressInput()
    }
    fun onCityEntered(city: String) {
        data = data.processNameChange(Intent.CityEntered(city))
        viewState.value = data.renderAddressInput()
    }
    fun onAddressEntered(address: String) {
        data = data.processNameChange(Intent.AddressEntered(address))
        viewState.value = data.renderAddressInput()
    }
    fun onNextEntered() {
        support.log(data.toString())
//        updateViewState { copy(next = R.id.action_userInputFragment2_to_userInputFragment3) }
    }
    // Изменяем данные
    private fun WizardData.processNameChange(event: Intent): WizardData = when (event) {
        is Intent.CountryEntered -> copy(country = event.country)
        is Intent.CityEntered -> copy(city = event.city)
        is Intent.AddressEntered -> copy(address = event.address)
        else -> TODO()
    }


    // Готовим данные для вью
    private fun WizardData.renderAddressInput(): ViewState.Address = ViewState.Address(
        country = country,
        countryError = support.isValidFields(country, false),
        city = city,
        cityError = support.isValidFields(city, false),
        address = address,
        addressError = support.isValidFields(address, false),
    )

    private fun updateViewState(block: ViewState.Address.() -> ViewState.Address) {
        val oldState = viewState.value!!
        val newState = block(oldState)
        viewState.value = newState
    }

//
//    val state: MutableLiveData<UserAddressState> = MutableLiveData(UserAddressState())
//
//    init {
//        updateViewState {
//            copy(
//                country = wizardCache.country,
//                city = wizardCache.city,
//                address = wizardCache.address,
//                check = wizardCache.check
//            )
//        }
//    }
//
//    override fun processIntent(intent: Intent) {
//        when (intent) {
//            is Intent.CountryNameChanged ->
//                updateViewState {
//                    copy(
//                        country = intent.country,
//                        countryError = support.isValidFields(intent.country, isClickFirst)
//                    )
//                }
//
//            is Intent.CityNameChanged ->
//                updateViewState {
//                    copy(
//                        city = intent.city,
//                        cityError = support.isValidFields(intent.city, isClickFirst)
//                    )
//                }
//
//            is Intent.AddressChanged ->
//                updateViewState {
//                    copy(
//                        address = intent.address,
//                        addressError = support.isValidFields(intent.address, isClickFirst)
//                    )
//                }
//
//            is Intent.NextButtonClicked -> {
//                val state = state.value!!
//                if (isClickFirst) {
//                    isClickFirst = false
//                    updateViewState {
//                        copy(
//                            countryError = support.isValidFields(state.country,isClickFirst),
//                            cityError = support.isValidFields(state.city,isClickFirst),
//                            addressError = support.isValidFields(state.address,isClickFirst)
//                        )
//                    }
//                }
//                validateAndSave()
//            }
//
//            else -> {}
//        }
//    }
//
//    fun updateViewState(block: UserAddressState.() -> UserAddressState) {
//        val oldState = state.value!!
//        val newState = block(oldState)
//        state.value = newState
//    }
//
//    override fun validateAndSave() {
//        val state = state.value
//        val isSave = support.validateResult(isValid(state))
//        if (isSave[0] == true) {
//            wizardCache.country = state!!.country
//            wizardCache.city = state.city
//            wizardCache.address = state.address
//            updateViewState {
//                copy(
//                    next = if (state.check)
//                        R.id.action_userInputFragment2_to_userInputFragment4
//                    else
//                        R.id.action_userInputFragment2_to_userInputFragment3
//                )
//            }
//            viewModelScope.launch {
//                _navigateToNextScreen.send(Unit)
//            }
//        }
//    }
//
//    private fun isValid(state: UserAddressState?): ValidateState? {
//        if (state == null)
//            return null
//        return if (state.countryError != null || state.cityError != null || state.addressError != null)
//            ValidateState.LoseFiled
//        else
//            ValidateState.Ok
//    }
}
