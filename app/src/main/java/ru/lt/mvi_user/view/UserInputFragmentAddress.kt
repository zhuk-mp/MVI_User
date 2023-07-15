package ru.lt.mvi_user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.UserInputIntent
import ru.lt.mvi_user.databinding.FragmentUserInput2Binding
import ru.lt.mvi_user.model.UserInputAddressViewModel


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

        lifecycleScope.launch  {
            viewModel.navigateToNextScreen.collect {
                findNavController().navigate(viewModel.state.value!!.next)
            }
        }

        // Задаем наблюдателей за изменением состояния
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
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
        })

        // Обрабатываем намерения пользователя
        binding.countryEditText.addTextChangedListener { text ->
            viewModel.processIntent(UserInputIntent.CountryNameChanged(text.toString()))
        }
        binding.cityEditText.addTextChangedListener { text ->
            viewModel.processIntent(UserInputIntent.CityNameChanged(text.toString()))
        }
        binding.addressEditText.addTextChangedListener { text ->
            viewModel.processIntent(UserInputIntent.AddressChanged(text.toString()))
        }
        binding.nextButton.setOnClickListener {
            viewModel.processIntent(UserInputIntent.NextButtonClicked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
