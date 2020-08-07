package com.rhymartmanchus.yelpassignment.data

import androidx.test.runner.AndroidJUnit4
import com.rhymartmanchus.yelpassignment.data.AndroidTestHelper.okHttpClient
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class CategoriesRemoteServiceProviderTest {

    companion object {
        const val API_KEY = "RAIonxmXViH_CSicOY-sPSPLbAhjZcE2-1NlTe25GM00Uop-HWMutrRswSwpBd5PfZbFaSikKPNqA_dQsZBjqOllTtU0LIy9SsQ5_Can177T8jeXrP5rYWbK4qQnX3Yx"
    }

    private lateinit var categoriesRemoteService: CategoriesRemoteService


    @Before
    fun setUp() {

        categoriesRemoteService = CategoriesRemoteServiceProvider(
            Retrofit.Builder()
                .baseUrl("https://api.yelp.com")
                .client(okHttpClient)
                .build()
        )

    }

    @Test
    fun fetchCategoriesByLocaleSuccessfully() = runBlocking {

        val result = categoriesRemoteService.fetchCategoriesByLocale(
            "en_CA"
        )

        assertTrue(result.isNotEmpty())
    }
}