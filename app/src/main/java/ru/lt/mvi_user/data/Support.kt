package ru.lt.mvi_user.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.lt.mvi_user.R
import ru.lt.mvi_user.state.ValidateState
import java.util.Calendar
import javax.inject.Inject

class Support @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    fun validateResult(valid: ValidateState?) : List<Boolean?> {
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

        return listOf(isSave, not18)
    }

    fun isCorrectDate(date: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val arrayDate = date.split(".")
        val d = if (arrayDate.getOrNull(0).isNullOrEmpty()) 1 else arrayDate.getOrNull(0)?.toInt()
        val m = if (arrayDate.getOrNull(1).isNullOrEmpty()) 1 else arrayDate.getOrNull(1)?.toInt()
        val y = if (arrayDate.getOrNull(2).isNullOrEmpty()) 1 else arrayDate.getOrNull(2)?.toInt()
        return (d in 1..31) && (m in 1..12) && (y in 1..currentYear)
    }


    fun isValidFields(fields: String, isClickFirst: Boolean, isDate: Boolean = false, isCorrectDate: Boolean = true): String? {
        return when {
            !isCorrectDate -> getString(R.string.forma_date)
            isClickFirst -> null
            fields.isEmpty() -> getString(R.string.not_lose)
            fields.length < 10 && isDate-> getString(R.string.forma_date)
            fields.length < 3 -> getString(R.string.min_length)
            else -> null
        }
    }

    fun log(str: String, toast: Boolean = false){
        if (!toast)
            Log.d("log----------------",str)
        else
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }


}