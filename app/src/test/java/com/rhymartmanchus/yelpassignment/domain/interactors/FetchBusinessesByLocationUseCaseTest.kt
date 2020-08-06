package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.Business
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.SocketTimeoutException

class FetchBusinessesByLocationUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var useCase: FetchBusinessesByLocationUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = FetchBusinessesByLocationUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    private suspend fun mockIdealRequest() {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                listOf(
                    mock(Business::class.java),
                    mock(Business::class.java)
                )
            }
    }

    @Test
    fun `should fetch businesses in a given coordinates`() = runBlocking {
        mockIdealRequest()

        val result = useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.000,
                123.000,
                SortingStrategy.Default
            )
        )

        assertEquals(2, result.businesses.size)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no businesses found in a given coordinates`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NoDataException("No businesses found")
            }

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.000,
                123.000,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = SocketTimeoutException::class)
    fun `should throw an exception when nothing is responded`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw SocketTimeoutException()
            }

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.000,
                123.000,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test
    fun `params for gateway method should include location coordinates and limit-offset`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.000,
                123.000,
                SortingStrategy.Default
            )
        )

        val captor =
            argumentCaptor<Map<String, String>>()
        verify(gateway).searchBusinesses(
            capture(
                captor
            )
        )

        assertEquals(
            mapOf(
                Pair("latitude", "13.0"),
                Pair("longitude", "123.0"),
                Pair("limit", "20"),
                Pair("offset", "0")
            ),
            captor.value
        )
    }

    @Test(expected = HttpRequestException::class)
    fun `should throw an exception when max limit value for api has exceeded the maximum = 50`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw HttpRequestException("VALIDATION_ERROR", "51 something", "limit")
            }

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                51,
                0,
                13.00,
                123.00,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test
    fun `should include to params sort_by=rating when value for sortingstrategy is rating`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.00,
                123.00,
                SortingStrategy.Rating
            )
        )

        val captor =
            argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(
            capture(
                captor
            )
        )

        assertTrue(captor.value.containsKey("sort_by"))
        assertTrue(captor.value["sort_by"]!! == "rating")
    }

    @Test
    fun `should include to params sort_by=distance when value for sortingstrategy is distance`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.00,
                123.00,
                SortingStrategy.Distance
            )
        )

        val captor =
            argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(
            capture(
                captor
            )
        )

        assertTrue(captor.value.containsKey("sort_by"))
        assertTrue(captor.value["sort_by"]!! == "distance")
    }

    @Test
    fun `should not include sort_by params when sortingstrategy is default`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesByLocationUseCase.Params(
                20,
                0,
                13.00,
                123.00,
                SortingStrategy.Default
            )
        )

        val captor =
            argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(
            capture(
                captor
            )
        )

        assertFalse(captor.value.containsKey("sort_by"))
    }
}