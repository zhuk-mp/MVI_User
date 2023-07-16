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
class UserInputTagsViewModel @Inject constructor(
    val data: RetainedWizardData,
    val support: Support
) : ViewModel() {

    val viewState: MutableLiveData<ViewState.Tag> = MutableLiveData()

    init {
        viewState.value = data.data.renderTagInput()
    }

    fun onTagEntered(tag: String, isSelected: Boolean) {
        data.data = data.data.processTagChange(Intent.TagEntered(tag, isSelected))
        viewState.value = data.data.renderTagInput()
    }
    fun onNextEntered() {
        viewState.value = data.data.renderTagInput()
        updateViewState {
            copy(
                next = if (data.data.check)
                    R.id.action_userInputFragment3_to_userInputFragment2
                else
                    R.id.action_userInputFragment3_to_userInputFragment4,
            )
        }
    }

    private fun WizardData.processTagChange(event: Intent.TagEntered): WizardData {
        val selectedTags = data.data.selectedTags
        if (event.isSelected)
            selectedTags.add(event.tag)
        else
            selectedTags.remove(event.tag)
        return copy(selectedTags = selectedTags)
    }

    private fun WizardData.renderTagInput(): ViewState.Tag = ViewState.Tag(
        selectedTags = selectedTags
    )

    fun updateViewState(block: ViewState.Tag.() -> ViewState.Tag) {
        val oldState = viewState.value!!
        val newState = block(oldState)
        viewState.value = newState
    }
}
