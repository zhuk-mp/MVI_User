package ru.lt.mvi_user.data

import android.util.Log
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RetainedWizardData @Inject constructor() {

    var data = WizardData()
    fun log(){
        Log.d("log----------------", data.toString())
    }
}