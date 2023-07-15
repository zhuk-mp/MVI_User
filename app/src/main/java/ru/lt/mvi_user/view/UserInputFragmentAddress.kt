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
import ru.lt.mvi_user.databinding.FragmentUserInput2Binding
import ru.lt.mvi_user.model.UserInputAddressViewModel
import ru.lt.mvi_user.state.ViewState


@AndroidEntryPoint
class UserInputFragmentAddress : Fragment(R.layout.fragment_user_input2) {
    private val viewModel: UserInputAddressViewModel by viewModels()
    private var _binding: FragmentUserInput2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInput2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
//            viewModel.support.log()
            viewModel.support.log("Address "+viewModel.data.toString())
            updateAddressInput(state)
            if (state.next != null)
//                viewModel.support.log(state.next.toString())
                findNavController().navigate(state.next)
        }


        binding.countryEditText.doAfterTextChanged {
            viewModel.onCountryEntered(it.toString())
        }

        binding.cityEditText.doAfterTextChanged {
            viewModel.onCityEntered(it.toString())
        }

        binding.addressEditText.doAfterTextChanged {
            viewModel.onAddressEntered(it.toString())
        }

        binding.nextButton.setOnClickListener {
            viewModel.onNextEntered()
        }
    }
    private fun updateAddressInput(state: ViewState.Address) {
        if (binding.countryEditText.text.toString() != state.country) {
            binding.countryEditText.setText(state.country, TextView.BufferType.EDITABLE)
        }
        if (binding.cityEditText.text.toString() != state.city) {
            binding.cityEditText.setText(state.city, TextView.BufferType.EDITABLE)
        }
        if (binding.addressEditText.text.toString() != state.address) {
            binding.addressEditText.setText(state.address, TextView.BufferType.EDITABLE)
        }
        binding.countryEditText.error = state.countryError
        binding.cityEditText.error = state.cityError
        binding.addressEditText.error = state.addressError
    }
//
//        // Задаем наблюдателей за изменением состояния
//        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
//            if (binding.countryEditText.text.toString() != state.country) {
//                binding.countryEditText.setText(state.country, TextView.BufferType.EDITABLE)
//            }
//            if (binding.cityEditText.text.toString() != state.city) {
//                binding.cityEditText.setText(state.city, TextView.BufferType.EDITABLE)
//            }
//            if (binding.addressEditText.text.toString() != state.address) {
//                binding.addressEditText.setText(state.address, TextView.BufferType.EDITABLE)
//            }
//            binding.countryEditText.error = state.countryError
//            binding.cityEditText.error = state.cityError
//            binding.addressEditText.error = state.addressError
//        })
//
//        // Обрабатываем намерения пользователя
//        binding.countryEditText.addTextChangedListener { text ->
//            viewModel.processIntent(Intent.CountryNameChanged(text.toString()))
//        }
//        binding.cityEditText.addTextChangedListener { text ->
//            viewModel.processIntent(Intent.CityNameChanged(text.toString()))
//        }
//        binding.addressEditText.addTextChangedListener { text ->
//            viewModel.processIntent(Intent.AddressChanged(text.toString()))
//        }
//        binding.nextButton.setOnClickListener {
//            viewModel.processIntent(Intent.NextButtonClicked)
//        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
