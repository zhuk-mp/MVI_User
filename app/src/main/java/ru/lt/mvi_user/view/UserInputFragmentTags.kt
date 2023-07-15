package ru.lt.mvi_user.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import ru.lt.mvi_user.R
import ru.lt.mvi_user.databinding.FragmentUserInput3Binding
import ru.lt.mvi_user.model.UserInputTagsViewModel
import ru.lt.mvi_user.state.ViewState


@AndroidEntryPoint
class UserInputFragmentTags : Fragment(R.layout.fragment_user_input3) {
    private val viewModel: UserInputTagsViewModel by viewModels()
    private var _binding: FragmentUserInput3Binding? = null
    private val binding get() = _binding!!

    private val tagTextViews = mutableListOf<TextView>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInput3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            updateTagInput(state)
            if (state.next != null) {
                if (state.selectedTags?.isNotEmpty() == true) {
                    findNavController().navigate(state.next)
                    viewModel.updateViewState { copy(next = null) }
                } else {
                    viewModel.support.log(viewModel.support.getString(R.string.selectedtags), true)
                    viewModel.updateViewState { copy(next = null) }
                }
            }
        }


        binding.nextButton.setOnClickListener {
            viewModel.onNextEntered()
        }

        val flexboxLayout = binding.tagContainer
        val tags = viewModel.support.context.resources.getStringArray(R.array.tags)
        tags.forEach { tag ->
            val textView = TextView(context).apply {
                text = tag
                textSize = 32f
                setTextColor(Color.WHITE)
                setPadding(30, 18, 30, 18)
                setBackgroundResource(R.drawable.tag_background)
                setOnClickListener {
                    it.isSelected = !it.isSelected
                    viewModel.onTagEntered(tag, it.isSelected)
                }

            }
            val lp = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(48, 48, 48, 48)
            tagTextViews.add(textView)
            flexboxLayout.addView(textView, lp)
        }
    }

    private fun updateTagInput(state: ViewState.Tag) {
        val selectedTags = state.selectedTags
        tagTextViews.forEach { textView ->
            val shouldSelect = selectedTags?.contains(textView.text) == true
            if (textView.isSelected != shouldSelect) {
                textView.isSelected = shouldSelect
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
