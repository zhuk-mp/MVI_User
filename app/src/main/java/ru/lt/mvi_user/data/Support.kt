package ru.lt.mvi_user.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.lt.mvi_user.R
import javax.inject.Inject

class Support @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    fun isValidFields(
        fields: String?,
        isClickFirst: Boolean,
        isDate: Boolean = false
    ): String? {
        if (isDate) log(fields ?: "null")
        var str = fields
        if (str == null) str = ""

        return when {
            isClickFirst -> null
            str.isEmpty() -> getString(R.string.not_lose)
            str.length < 3 -> getString(R.string.min_length)
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