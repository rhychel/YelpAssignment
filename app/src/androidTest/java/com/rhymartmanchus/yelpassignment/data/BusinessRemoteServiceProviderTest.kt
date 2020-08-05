package com.rhymartmanchus.yelpassignment.data

import androidx.test.runner.AndroidJUnit4
import com.rhymartmanchus.yelpassignment.BuildConfig
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit


@RunWith(AndroidJUnit4::class)
class BusinessRemoteServiceProviderTest {

    companion object {
        const val API_KEY = "RAIonxmXViH_CSicOY-sPSPLbAhjZcE2-1NlTe25GM00Uop-HWMutrRswSwpBd5PfZbFaSikKPNqA_dQsZBjqOllTtU0LIy9SsQ5_Can177T8jeXrP5rYWbK4qQnX3Yx"
    }
    private lateinit var businessesRemoteService: BusinessesRemoteService

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor { chain ->
            val request: Request.Builder = chain.request()
                .newBuilder()
            request.addHeader("Authorization", "Bearer $API_KEY")

            chain.proceed(request.build())
        }
        .build()

    @Before
    fun setUp() {
        businessesRemoteService = BusinessRemoteServiceProvider(
            Retrofit.Builder()
                .baseUrl("https://api.yelp.com")
                .client(okHttpClient)
                .build()
        )

    }

    @Test
    fun fetchBusinessesSuccessfully() = runBlocking {

        val result = businessesRemoteService.fetchBusinesses(
            mapOf(
                Pair("location", "Camarines Sur")
            )
        )

        assertTrue(result.isNotEmpty())
        assertEquals(20, result.size)
    }
}