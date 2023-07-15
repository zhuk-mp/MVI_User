package ru.lt.mvi_user.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingletonWizardData @Inject constructor() {
    var wizardData = WizardData()
}