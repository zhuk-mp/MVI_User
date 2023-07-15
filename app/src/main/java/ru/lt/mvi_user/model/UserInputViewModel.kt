package ru.lt.mvi_user.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.Intent
import ru.lt.mvi_user.data.SingletonWizardData
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.WizardData
import ru.lt.mvi_user.state.ViewState
import javax.inject.Inject

@HiltViewModel
class UserInputViewModel @Inject constructor(
    singletonWizardData: SingletonWizardData,
    val support: Support
) : ViewModel()
{
    var data: WizardData = singletonWizardData.wizardData


    val viewState: MutableLiveData<ViewState.Name> = MutableLiveData()

    fun onNameEntered(name: String) {
        data = data.processNameChange(Intent.NameEntered(name))
        viewState.value = data.renderNameInput()
    }
    fun onLastNameEntered(lastName: String) {
        data = data.processNameChange(Intent.LastNameEntered(lastName))
        viewState.value = data.renderNameInput()
    }

    fun onBdEntered(date: String) {
        data = data.processNameChange(Intent.BdEntered(date))
        viewState.value = data.renderNameInput()
    }
    fun onNextEntered() {
        support.log(data.toString())
        updateViewState { copy(next = R.id.action_userInputFragment_to_userInputFragment2) }
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
        nameError = support.isValidFields(name, false),
        lastName = lastName,
        lastNameError = support.isValidFields(lastName, false),
        bd = bd,
        bdError = support.isValidFields(bd, false),
    )

    private fun updateViewState(block: ViewState.Name.() -> ViewState.Name) {
        val oldState = viewState.value!!
        val newState = block(oldState)
        viewState.value = newState
    }

//    override fun processIntent(intent: Intent) {
//        when (intent) {
//            is Intent.FirstNameChanged ->
//                updateViewState {
//                    copy(
//                        firstName = intent.firstName,
//                        firstNameError = support.isValidFields(intent.firstName, isClickFirst)
//                    )
//                }
//
//            is Intent.LastNameChanged ->
//                updateViewState {
//                    copy(
//                        lastName = intent.lastName,
//                        lastNameError = support.isValidFields(intent.lastName, isClickFirst)
//                    )
//                }
//
//            is Intent.BateOfBirthChanged ->
//                updateViewState {
//                    copy(
//                        dateOfBirth = intent.dateOfBirth,
//                        dateOfBirthError = support.isValidFields(
//                            intent.dateOfBirth,
//                            isClickFirst,
//                            true,
//                            support.isCorrectDate(intent.dateOfBirth)
//                        )
//                    )
//                }
//
//            is Intent.SwitchFragmentsChanged ->
//                updateViewState { copy(check = intent.check) }
//
//            is Intent.NextButtonClicked -> {
//                val state = state.value!!
//                if (isClickFirst) {
//                    isClickFirst = false
//                    updateViewState {
//                        copy(
//                            firstNameError = support.isValidFields(state.firstName,isClickFirst),
//                            lastNameError = support.isValidFields(state.lastName,isClickFirst),
//                            dateOfBirthError = support.isValidFields(state.dateOfBirth,isClickFirst)
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
//    fun updateViewState(block: UserDateState.() -> UserDateState) {
//        val oldState = state.value!!
//        val newState = block(oldState)
//        state.value = newState
//    }
//
//    override fun validateAndSave() {
//        val state = state.value
//        val isSave = support.validateResult(isValid(state))
//        val not18 = isSave[1]
//        updateViewState { copy(not18 = not18) }
//        if (not18 != null) {
//            support.log(
//                support.getString(if (not18) R.string.not18_error else R.string.error),
//                true
//            )
//            updateViewState { copy(not18 = null) }
//        }
//        if (isSave[0] == true) {
//            wizardCache.firstName = state!!.firstName
//            wizardCache.lastName = state.lastName
//            wizardCache.dateOfBirth = state.dateOfBirth
//            wizardCache.check = state.check
//            updateViewState {
//                copy(
//                    next = if (state.check)
//                        R.id.action_userInputFragment_to_userInputFragment3
//                    else
//                        R.id.action_userInputFragment_to_userInputFragment2,
//                )
//            }
//            viewModelScope.launch {
//                _navigateToNextScreen.send(Unit)
//            }
//        }
//    }
//
//    private fun isValid(state: UserDateState?): ValidateState? {
//        if (state == null)
//            return null
//        if (state.firstNameError != null || state.lastNameError != null || state.dateOfBirthError != null || !support.isCorrectDate(
//                state.dateOfBirth
//            )
//        )
//            return ValidateState.LoseFiled
//
//        val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
//
//        try {
//            val date = format.parse(state.dateOfBirth)
//
//            val calendar = Calendar.getInstance()
//            calendar.time = date
//
//            val cal = Calendar.getInstance()
//            cal.time = date
//            cal.add(Calendar.YEAR, 18)
//            return if (cal.time.before(Date()))
//                ValidateState.Ok
//            else
//                ValidateState.Not18
//
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return ValidateState.LoseFiled
//    }
}
