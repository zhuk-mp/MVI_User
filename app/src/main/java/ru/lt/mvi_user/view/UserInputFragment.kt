package ru.lt.mvi_user.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.lt.mvi_user.R
import ru.lt.mvi_user.databinding.FragmentUserInputBinding
import ru.lt.mvi_user.model.UserInputViewModel
import ru.lt.mvi_user.state.ViewState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class UserInputFragment : Fragment(R.layout.fragment_user_input) {
    private val viewModel: UserInputViewModel by viewModels()
    private var _binding: FragmentUserInputBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateNameInput(state)
            if (state.next != null) {
                if (state.nameError == null && state.lastNameError == null && state.bdError == null)
                    findNavController().navigate(state.next)
                else viewModel.updateViewState { copy(next = null) }
            }
//
            if (state.not18 == false) {
                binding.nextButton.isEnabled = false
                viewModel.support.log(viewModel.support.getString(R.string.not18_error), true)
            }
        }


        binding.firstNameEditText.doAfterTextChanged {
            viewModel.onNameEntered(it.toString())
        }

        binding.lastNameEditText.doAfterTextChanged {
            viewModel.onLastNameEntered(it.toString())
        }
        binding.dateOfBirthEditText.apply {

            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = true

            setOnClickListener {
                val calendar = Calendar.getInstance()

                viewModel.viewState.value!!.bd?.let {
                    calendar.time = it
                }

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        val selectedDate = calendar.time
                        viewModel.onBdEntered(selectedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.show()
            }
        }
        binding.switchFragments.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSwitchEntered(isChecked)
        }
        binding.nextButton.setOnClickListener {
            viewModel.onNextEntered()
        }
    }
    private fun updateNameInput(state: ViewState.Name) {
        if (binding.firstNameEditText.text.toString() != state.name) {
            binding.firstNameEditText.setText(state.name, TextView.BufferType.EDITABLE)
        }
        if (binding.lastNameEditText.text.toString() != state.lastName) {
            binding.lastNameEditText.setText(state.lastName, TextView.BufferType.EDITABLE)
        }
        val date =
            state.bd?.let { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it) }
        if (binding.dateOfBirthEditText.text.toString() != date) {
            binding.nextButton.isEnabled = true
            binding.dateOfBirthEditText.setText(date, TextView.BufferType.EDITABLE)
        }
        binding.firstNameEditText.error = state.nameError
        binding.lastNameEditText.error = state.lastNameError
        binding.dateOfBirthEditText.error = state.bdError
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
