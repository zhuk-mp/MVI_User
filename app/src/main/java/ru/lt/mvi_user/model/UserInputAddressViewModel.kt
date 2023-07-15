package ru.lt.mvi_user.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.Intent
import ru.lt.mvi_user.data.RetainedWizardData
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.WizardData
import ru.lt.mvi_user.state.ViewState
import javax.inject.Inject

@HiltViewModel
class UserInputAddressViewModel @Inject constructor(
    val data: RetainedWizardData,
    val support: Support
) : ViewModel()
{

    val viewState: MutableLiveData<ViewState.Address> = MutableLiveData()
    private var isClickFirst = true
    init {
        viewState.value = data.data.renderAddressInput()
    }

    fun onCountryEntered(country: String) {
        data.data = data.data.processNameChange(Intent.CountryEntered(country))
        viewState.value = data.data.renderAddressInput()
    }
    fun onCityEntered(city: String) {
        data.data = data.data.processNameChange(Intent.CityEntered(city))
        viewState.value = data.data.renderAddressInput()
    }
    fun onAddressEntered(address: String) {
        data.data = data.data.processNameChange(Intent.AddressEntered(address))
        viewState.value = data.data.renderAddressInput()
    }
    fun onNextEntered() {
        isClickFirst = false
        data.log()
        viewState.value = data.data.renderAddressInput()
        updateViewState {
            copy(
                next = if (data.data.check)
                    R.id.action_userInputFragment2_to_userInputFragment4
                else
                    R.id.action_userInputFragment2_to_userInputFragment3,
            )
        }
        support.log(viewState.value.toString())
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
        countryError = support.isValidFields(country, isClickFirst),
        city = city,
        cityError = support.isValidFields(city, isClickFirst),
        address = address,
        addressError = support.isValidFields(address, isClickFirst),
    )

    fun updateViewState(block: ViewState.Address.() -> ViewState.Address) {
        val oldState = viewState.value!!
        val newState = block(oldState)
        viewState.value = newState
    }

}
