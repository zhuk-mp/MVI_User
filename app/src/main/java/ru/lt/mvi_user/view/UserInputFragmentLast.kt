package ru.lt.mvi_user.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.lt.mvi_user.R
import ru.lt.mvi_user.databinding.FragmentUserInput4Binding
import ru.lt.mvi_user.model.UserInputLastViewModel
import ru.lt.mvi_user.state.ViewState


@AndroidEntryPoint
class UserInputFragmentLast : Fragment(R.layout.fragment_user_input4) {
    private val viewModel: UserInputLastViewModel by viewModels()
    private var _binding: FragmentUserInput4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInput4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateLastInput(state)
        }

    }

    private fun updateLastInput(state: ViewState.Last) {
        binding.firstNameTextView.text = state.name
        binding.lastNameTextView.text = state.lastName
        binding.dateOfBirthTextView.text = state.bd
        binding.addressTextView.text = state.fullAddress

        state.selectedTags?.forEach { tag ->
            val textView = TextView(context).apply {
                text = tag
                textSize = 24f
                setTextColor(Color.WHITE)
                setPadding(20, 12, 20, 12)
                setBackgroundResource(R.drawable.tag_background2)

            }
            val lp = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(30, 30, 30, 30)
            binding.tagContainer.addView(textView, lp)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
