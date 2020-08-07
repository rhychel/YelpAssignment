package com.rhymartmanchus.yelpassignment.data

import okhttp3.OkHttpClient
import okhttp3.Request

object AndroidTestHelper {
    const val API_KEY = "RAIonxmXViH_CSicOY-sPSPLbAhjZcE2-1NlTe25GM00Uop-HWMutrRswSwpBd5PfZbFaSikKPNqA_dQsZBjqOllTtU0LIy9SsQ5_Can177T8jeXrP5rYWbK4qQnX3Yx"

    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor { chain ->
            val request: Request.Builder = chain.request()
                .newBuilder()
            request.addHeader("Authorization", "Bearer $API_KEY")

            chain.proceed(request.build())
        }
        .build()
}