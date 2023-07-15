package ru.lt.mvi_user.model

import android.icu.util.Calendar
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
import ru.lt.mvi_user.state.UserDateState
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
    private var isClickFirst = true

    val state: MutableLiveData<UserDateState> = MutableLiveData(UserDateState())

    init {
        updateViewState {
            copy(
                firstName = wizardCache.firstName,
                lastName = wizardCache.firstName,
                dateOfBirth = wizardCache.firstName,
                check = wizardCache.check
            )
        }
    }

    override fun processIntent(intent: UserInputIntent) {
        when (intent) {
            is UserInputIntent.FirstNameChanged ->
                updateViewState {
                    copy(
                        firstName = intent.firstName,
                        firstNameError = support.isValidFields(intent.firstName, isClickFirst)
                    )
                }

            is UserInputIntent.LastNameChanged ->
                updateViewState {
                    copy(
                        lastName = intent.lastName,
                        lastNameError = support.isValidFields(intent.lastName, isClickFirst)
                    )
                }

            is UserInputIntent.BateOfBirthChanged ->
                updateViewState {
                    copy(
                        dateOfBirth = intent.dateOfBirth,
                        dateOfBirthError = support.isValidFields(
                            intent.dateOfBirth,
                            isClickFirst,
                            true,
                            support.isCorrectDate(intent.dateOfBirth)
                        )
                    )
                }

            is UserInputIntent.SwitchFragmentsChanged ->
                updateViewState { copy(check = intent.check) }

            is UserInputIntent.NextButtonClicked -> {
                val state = state.value!!
                if (isClickFirst) {
                    isClickFirst = false
                    updateViewState {
                        copy(
                            firstNameError = support.isValidFields(state.firstName,isClickFirst),
                            lastNameError = support.isValidFields(state.lastName,isClickFirst),
                            dateOfBirthError = support.isValidFields(state.dateOfBirth,isClickFirst)
                        )
                    }
                }
                validateAndSave()
            }

            else -> {}
        }
    }

    fun updateViewState(block: UserDateState.() -> UserDateState) {
        val oldState = state.value!!
        val newState = block(oldState)
        state.value = newState
    }

    override fun validateAndSave() {
        val state = state.value
        val isSave = support.validateResult(isValid(state))
        val not18 = isSave[1]
        updateViewState { copy(not18 = not18) }
        if (not18 != null) {
            support.log(
                support.getString(if (not18) R.string.not18_error else R.string.error),
                true
            )
            updateViewState { copy(not18 = null) }
        }
        if (isSave[0] == true) {
            wizardCache.firstName = state!!.firstName
            wizardCache.lastName = state.lastName
            wizardCache.dateOfBirth = state.dateOfBirth
            wizardCache.check = state.check
            updateViewState {
                copy(
                    next = if (state.check)
                        R.id.action_userInputFragment_to_userInputFragment3
                    else
                        R.id.action_userInputFragment_to_userInputFragment2,
                )
            }
            viewModelScope.launch {
                _navigateToNextScreen.send(Unit)
            }
        }
    }

    private fun isValid(state: UserDateState?): ValidateState? {
        if (state == null)
            return null
        if (state.firstNameError != null || state.lastNameError != null || state.dateOfBirthError != null || !support.isCorrectDate(
                state.dateOfBirth
            )
        )
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
