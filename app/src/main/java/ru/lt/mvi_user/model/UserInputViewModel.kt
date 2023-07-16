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
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UserInputViewModel @Inject constructor(
    val data: RetainedWizardData,
    val support: Support
) : ViewModel()
{

    val viewState: MutableLiveData<ViewState.Name> = MutableLiveData()
    private var isClickFirst = true

    init {
        viewState.value = data.data.renderNameInput()
    }

    fun onNameEntered(name: String) {
        data.data = data.data.processNameChange(Intent.NameEntered(name))
        viewState.value = data.data.renderNameInput()
    }
    fun onLastNameEntered(lastName: String) {
        data.data = data.data.processNameChange(Intent.LastNameEntered(lastName))
        viewState.value = data.data.renderNameInput()
    }
    fun onSwitchEntered(check: Boolean) {
        data.data = data.data.processNameChange(Intent.SwitchEntered(check))
        viewState.value = data.data.renderNameInput()
    }
    fun onBdEntered(date: Date) {
        data.data = data.data.processNameChange(Intent.BdEntered(date))
        viewState.value = data.data.renderNameInput()
    }
    fun onNextEntered() {
        isClickFirst = false
        viewState.value = data.data.renderNameInput()
        updateViewState {
                copy(
                    next = if (data.data.check)
                        R.id.action_userInputFragment_to_userInputFragment3
                    else
                        R.id.action_userInputFragment_to_userInputFragment2,
                )
            }
    }
    // Изменяем данные
    private fun WizardData.processNameChange(event: Intent): WizardData = when (event) {
        is Intent.NameEntered -> copy(name = event.name)
        is Intent.LastNameEntered -> copy(lastName = event.lastName)
        is Intent.BdEntered -> copy(bd = event.date)
        is Intent.SwitchEntered -> copy(check = event.check)
        else -> TODO()
    }
    // Готовим данные для вью
    private fun WizardData.renderNameInput(): ViewState.Name = ViewState.Name(
        name = name,
        nameError = support.isValidFields(name, isClickFirst),
        lastName = lastName,
        lastNameError = support.isValidFields(lastName, isClickFirst),
        bd = bd,
        bdError = if (bd == null && !isClickFirst) support.getString(R.string.not_lose) else null,
        not18 = bd?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            ?.isBefore(LocalDate.now().minusYears(18)),
        check = check
    )

    fun updateViewState(block: ViewState.Name.() -> ViewState.Name) {
        val oldState = viewState.value!!
        val newState = block(oldState)
        viewState.value = newState
    }
}
