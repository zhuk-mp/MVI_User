package ru.lt.mvi_user.view

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
            viewModel.support.log("User "+viewModel.data.toString())
            updateNameInput(state)
            if (state.next != null)
                findNavController().navigate(state.next)
        }


        binding.firstNameEditText.doAfterTextChanged {
            viewModel.onNameEntered(it.toString())
        }

        binding.lastNameEditText.doAfterTextChanged {
            viewModel.onLastNameEntered(it.toString())
        }

        binding.dateOfBirthEditText.doAfterTextChanged {
            viewModel.onBdEntered(it.toString())
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
        if (binding.dateOfBirthEditText.text.toString() != state.bd) {
            binding.dateOfBirthEditText.setText(state.bd, TextView.BufferType.EDITABLE)
        }
        binding.firstNameEditText.error = state.nameError
        binding.lastNameEditText.error = state.lastNameError
        binding.dateOfBirthEditText.error = state.bdError
    }

//        // Задаем наблюдателей за изменением состояния
//        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
//            if (binding.firstNameEditText.text.toString() != state.firstName) {
//                binding.firstNameEditText.setText(state.firstName, TextView.BufferType.EDITABLE)
//            }
//            if (binding.lastNameEditText.text.toString() != state.lastName) {
//                binding.lastNameEditText.setText(state.lastName, TextView.BufferType.EDITABLE)
//            }
//            if (binding.dateOfBirthEditText.text.toString() != state.dateOfBirth) {
//                binding.dateOfBirthEditText.setText(state.dateOfBirth, TextView.BufferType.EDITABLE)
//            }
//            if (binding.switchFragments.isChecked != state.check) {
//                binding.switchFragments.isChecked = state.check
//            }
//            binding.firstNameEditText.error = state.firstNameError
//            binding.lastNameEditText.error = state.lastNameError
//            binding.dateOfBirthEditText.error = state.dateOfBirthError
//            if (state.not18 != null)
//                    binding.nextButton.isEnabled = !state.not18
//        })
//
//        // Обрабатываем намерения пользователя
//        binding.firstNameEditText.addTextChangedListener { text ->
//            viewModel.processIntent(Intent.FirstNameChanged(text.toString()))
//        }
//        binding.lastNameEditText.addTextChangedListener { text ->
//            viewModel.processIntent(Intent.LastNameChanged(text.toString()))
//        }
//        binding.switchFragments.setOnCheckedChangeListener { _, isChecked ->
//            viewModel.processIntent(Intent.SwitchFragmentsChanged(isChecked))
//        }
//        binding.dateOfBirthEditText.filters = arrayOf<InputFilter>(
//            InputFilter.LengthFilter(10),
//            InputFilter { source, _, _, _, _, _ ->
//                if (source in ". /-") {
//                    return@InputFilter ""
//                }
//                null
//            }
//        )
//        binding.dateOfBirthEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.nextButton.isEnabled = true
//                if (start != s.length && (s.length == 2 || s.length == 5)) {
//                    binding.dateOfBirthEditText.setText(viewModel.support.getString(R.string.formatDate, s))
//                    binding.dateOfBirthEditText.setSelection(s.length + 1)
//                }
//            }
//            override fun afterTextChanged(s: Editable?) {
//                viewModel.processIntent(Intent.BateOfBirthChanged(binding.dateOfBirthEditText.text.toString()))
//            }
//        })
//        binding.nextButton.setOnClickListener {
//            viewModel.processIntent(Intent.NextButtonClicked)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
