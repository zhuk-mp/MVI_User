package ru.lt.mvi_user.model

import android.icu.util.Calendar
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserInputViewModel @Inject constructor(
    private val wizardCache: WizardCache,
    val support: Support
) : ViewModel(), IUserInputViewModel {

    private val _navigateToNextScreen = Channel<Unit>(Channel.BUFFERED)
    val navigateToNextScreen = _navigateToNextScreen.receiveAsFlow()

    override fun processIntent(intent: UserInputIntent) {
        when (intent) {
            is UserInputIntent.FirstNameChanged ->
                support.updateViewState {copy(firstName = intent.firstName, firstNameError = support.isValidFields(intent.firstName))}
            is UserInputIntent.LastNameChanged ->
                support.updateViewState {copy(lastName = intent.lastName, lastNameError = support.isValidFields(intent.lastName))}
            is UserInputIntent.BateOfBirthChanged ->
                support.updateViewState {copy(dateOfBirth = intent.dateOfBirth, dateOfBirthError = support.isValidFields(intent.dateOfBirth, true, support.isCorrectDate(intent.dateOfBirth)))}
            is UserInputIntent.SwitchFragmentsChanged ->
                support.updateViewState {copy(check = intent.check)}
            is UserInputIntent.NextButtonClicked -> {
                val state = support.state.value!!
                if (!state.isClickFirst) {
                    support.updateViewState {copy(isClickFirst = true)}
                    support.updateViewState {
                        copy(
                            firstNameError = support.isValidFields(state.firstName),
                            lastNameError = support.isValidFields(state.lastName),
                            dateOfBirthError = support.isValidFields(state.dateOfBirth)
                            )
                    }
                }
                validateAndSave()
            }
            else -> {}
        }
    }

    override fun validateAndSave() {
        val state =  support.state.value
        val isSave = support.validateResult(isValid(state))
        if (isSave) {
            wizardCache.firstName = state!!.firstName
            wizardCache.lastName = state.lastName
            wizardCache.dateOfBirth = state.dateOfBirth
            support.updateViewState {
                copy(next = if (state.check)
                    R.id.action_userInputFragment_to_userInputFragment3
                else
                    R.id.action_userInputFragment_to_userInputFragment2,
                    isClickFirst = false)
            }
            viewModelScope.launch {
                _navigateToNextScreen.send(Unit)
            }
        }
    }

    override fun isValid(state: ViewState?): ValidateState? {
        if (state == null)
            return null
        if (state.firstNameError != null || state.lastNameError != null || state.dateOfBirthError != null || !support.isCorrectDate(state.dateOfBirth))
            return ValidateState.LoseFiled

        val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)

        try {
            val date = format.parse(state.dateOfBirth)

            val calendar = Calendar.getInstance()
            calendar.time = date

            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.YEAR, 18)
            return if (cal.time.before(Date()))
                ValidateState.Ok
            else
                ValidateState.Not18

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ValidateState.LoseFiled
    }
}
