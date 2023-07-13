package ru.lt.mvi_user.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.lt.mvi_user.R
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Support @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    val state: MutableLiveData<ViewState> = MutableLiveData(ViewState())

    fun validateResult(valid: ValidateState?) :Boolean {
        var isSave = false
        val not18 = when (valid) {
            is ValidateState.LoseFiled -> null
            ValidateState.Not18 -> true
            ValidateState.Ok -> {
                isSave = true
                null
            }
            null -> false
        }
        updateViewState {copy(not18 = not18)}
        if (not18 != null) {
            log(getString(if (not18) R.string.not18_error else R.string.error), true)
            updateViewState {copy(not18 = null)}
        }
        return isSave
    }

    fun isCorrectDate(date: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val arrayDate = date.split(".")
        val d = if (arrayDate.getOrNull(0).isNullOrEmpty()) 1 else arrayDate.getOrNull(0)?.toInt()
        val m = if (arrayDate.getOrNull(1).isNullOrEmpty()) 1 else arrayDate.getOrNull(1)?.toInt()
        val y = if (arrayDate.getOrNull(2).isNullOrEmpty()) 1 else arrayDate.getOrNull(2)?.toInt()
        return (d in 1..31) && (m in 1..12) && (y in 1..currentYear)
    }

    fun updateViewState(block: ViewState.() -> ViewState) {
        val oldState = state.value!!
        val newState = block(oldState)
        state.value = newState
    }

    fun isValidFields(fields: String, isDate: Boolean = false, isCorrectDate: Boolean = true): String? {
        return when {
            !isCorrectDate -> getString(R.string.forma_date)
            !state.value!!.isClickFirst -> null
            fields.isEmpty() -> getString(R.string.not_lose)
            fields.length < 10 && isDate-> getString(R.string.forma_date)
            fields.length < 3 -> getString(R.string.min_length)
            else -> null
        }
    }

    fun log(str: String? = null, toast: Boolean = false){
        if (!toast)
            Log.d("log----------------",str ?: "${state.value}")
        else
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
}