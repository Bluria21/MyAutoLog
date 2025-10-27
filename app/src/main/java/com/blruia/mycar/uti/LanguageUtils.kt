package com.blruia.mycar.uti

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

    fun setAppLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val resources = context.resources
    val config = Configuration(resources.configuration)
    config.setLocale(locale)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.createConfigurationContext(config)
    }

    resources.updateConfiguration(config, resources.displayMetrics)
}

// ВНЕ функции setAppLocale
fun updateContextLocale(context: Context, languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}