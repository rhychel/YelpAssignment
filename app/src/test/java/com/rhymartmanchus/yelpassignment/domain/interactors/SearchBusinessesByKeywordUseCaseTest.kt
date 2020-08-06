package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.MaxLimitParamException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.Business
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class SearchBusinessesByKeywordUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var useCase: SearchBusinessesByKeywordUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = SearchBusinessesByKeywordUseCase(
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
    fun `should fetch businesses by keyword in a location successfully`() = runBlocking {
        mockIdealRequest()

        val result = useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "Sample",
                1,
                1,
                12.99,
                123.92,
                SortingStrategy.Default
            )
        )

        assertEquals(2, result.businesses.size)
    }

    @Test
    fun `should include term, coordinates, limit and offset in params when searching for a keyword`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "keyword",
                1,
                1,
                13.999,
                123.111,
                SortingStrategy.Default
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("term"))
        assertTrue(captor.value.containsKey("latitude"))
        assertTrue(captor.value.containsKey("longitude"))
        assertTrue(captor.value.containsKey("limit"))
        assertTrue(captor.value.containsKey("offset"))
        assertTrue(!captor.value.containsKey("sort_by"))
    }

    @Test
    fun `should include sort_by=distance when trying to sort results by distance`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "keyword",
                1,
                1,
                13.999,
                123.111,
                SortingStrategy.Distance
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("sort_by"))
        assertEquals("distance", captor.value.get("sort_by"))
    }

    @Test
    fun `should include sort_by=rating when trying to sort results by rating`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "keyword",
                1,
                1,
                13.999,
                123.111,
                SortingStrategy.Rating
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("sort_by"))
        assertEquals("rating", captor.value.get("sort_by"))
    }

    @Test(expected = NetworkErrorException::class)
    fun `should throw an exception when ioexception happened`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NetworkErrorException(IOException())
            }

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "Sample",
                1,
                1,
                12.12,
                123.123,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = MaxLimitParamException::class)
    fun `should throw an exception when limit param reached the maximum value 50`() = runBlocking {
        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "Sample",
                51,
                1,
                12.12,
                123.123,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no results for which relates to the keyword`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NoDataException("No business is fetched")
            }

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "Sample",
                1,
                2,
                12.213,
                212.312,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw an exception when keyword is empty`() = runBlocking {

        useCase.execute(
            SearchBusinessesByKeywordUseCase.Params(
                "",
                1,
                1,
                12.12,
                123.123,
                SortingStrategy.Default
            )
        )

        Unit
    }

}