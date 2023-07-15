package ru.lt.mvi_user.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.RetainedWizardData
import ru.lt.mvi_user.data.Support
import ru.lt.mvi_user.data.WizardData
import ru.lt.mvi_user.state.ViewState
import javax.inject.Inject

@HiltViewModel
class UserInputLastViewModel @Inject constructor(
    val data: RetainedWizardData,
    val support: Support
) : ViewModel()
{

    val viewState: MutableLiveData<ViewState.Last> = MutableLiveData()

    init {
        viewState.value = data.data.renderLastInput()
    }

    private fun WizardData.renderLastInput(): ViewState.Last = ViewState.Last(
        name = name,
        lastName = lastName,
        bd = bd,
        fullAddress = support.context.resources.getString(
            R.string.full_address, country, city, address
        ),
        selectedTags = selectedTags,
    )
}
