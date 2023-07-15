package ru.lt.mvi_user.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexboxLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.lt.mvi_user.R
import ru.lt.mvi_user.data.Intent
import ru.lt.mvi_user.databinding.FragmentUserInput3Binding
import ru.lt.mvi_user.model.UserInputTagsViewModel


@AndroidEntryPoint
class UserInputFragmentTags : Fragment(R.layout.fragment_user_input3) {
    private val viewModel: UserInputTagsViewModel by viewModels()
    private var _binding: FragmentUserInput3Binding? = null
    private val binding get() = _binding!!

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentUserInput3Binding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launch  {
//            viewModel.navigateToNextScreen.collect {
//                findNavController().navigate(viewModel.state.value!!.next)
//            }
//        }
//
//        val tagTextViews = mutableListOf<TextView>()
//
//        // Задаем наблюдателей за изменением состояния
//        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
//            val selectedTags = state.selectedTags
//            tagTextViews.forEach { textView ->
//                val shouldSelect = textView.text in selectedTags
//                if (textView.isSelected != shouldSelect) {
//                    textView.isSelected = shouldSelect
//                }
//            }
//        })
//
//        // Обрабатываем намерения пользователя
//        val flexboxLayout = binding.tagContainer
//        val tags = viewModel.support.context.resources.getStringArray(R.array.tags)
//        tags.forEach { tag ->
//            val textView = TextView(context).apply {
//                text = tag
//                textSize = 32f
//                setTextColor(Color.WHITE)
//                setPadding(30, 18, 30, 18)
//                setBackgroundResource(R.drawable.tag_background)
//                setOnClickListener {
//                    it.isSelected = !it.isSelected
//                    viewModel.processIntent(Intent.TagChanged(tag, it.isSelected))
//                }
//
//            }
//            val lp = FlexboxLayout.LayoutParams(
//                FlexboxLayout.LayoutParams.WRAP_CONTENT,
//                FlexboxLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp.setMargins(48, 48, 48, 48)
//            tagTextViews.add(textView)
//            flexboxLayout.addView(textView, lp)
//        }
//
//        binding.nextButton.setOnClickListener {
//            viewModel.processIntent(Intent.NextButtonClicked)
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
