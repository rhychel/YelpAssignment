package com.rhymartmanchus.yelpassignment.data

import androidx.test.runner.AndroidJUnit4
import com.rhymartmanchus.yelpassignment.data.AndroidTestHelper.okHttpClient
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
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
class BusinessesRemoteServiceProviderTest {

    private lateinit var businessesRemoteService: BusinessesRemoteService


    @Before
    fun setUp() {
        businessesRemoteService = BusinessesRemoteServiceProvider(
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

    @Test
    fun throwAndExceptionWhenLimitValueExceedsMaximum() = runBlocking {
        try {
            businessesRemoteService.fetchBusinesses(
                mapOf(
                    Pair("location", "Camarines Sur"),
                    Pair("limit", "51")
                )
            )
        } catch (e: HttpRequestException) {
            assertTrue(e.field == "limit")
        }

        Unit
    }

    @Test
    fun fetchBusinessByAlias() = runBlocking {
        val result = businessesRemoteService.fetchByAlias("appleberry-iphone-and-mac-repair-new-york-4")

        assertEquals("appleberry-iphone-and-mac-repair-new-york-4", result.alias)
    }
}