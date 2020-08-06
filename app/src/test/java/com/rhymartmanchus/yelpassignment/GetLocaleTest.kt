package com.rhymartmanchus.yelpassignment

import org.junit.Test
import java.util.*

class GetLocaleTest {

    @Test
    fun getDeviceLocale() {

        Locale.getAvailableLocales().forEach {
            println("Locale: ${it.toLanguageTag().replace("-", "_")}")
        }

        println("Country: ${Locale.getDefault().toLanguageTag().replace("-", "_")}")

    }

}