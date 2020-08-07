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
import com.rhymartmanchus.yelpassignment.domain.models.Category
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.net.SocketTimeoutException

class FetchBusinessesInCategoryByLocationUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var category: Category
    private lateinit var useCase: FetchBusinessesInCategoryByLocationUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        category = Category(
            "my-category",
            "My Category",
            listOf(
                "1-2"
            )
        )

        useCase = FetchBusinessesInCategoryByLocationUseCase(
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
    fun `should fetch businesses on a category with a given coordinates`() = runBlocking {

        mockIdealRequest()

        val result = useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                1,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        assertEquals(2, result.businesses.size)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no businesses found on a category in a given coordinates`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NoDataException("No businesses available")
            }

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                1,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = NetworkErrorException::class)
    fun `should throw an exception when nothing is responded`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NetworkErrorException(SocketTimeoutException())
            }

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                1,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test
    fun `params for gateway method should include location coordinates, category and limit-offset`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                50,
                0,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("categories"))
        assertTrue(captor.value.containsKey("latitude"))
        assertTrue(captor.value.containsKey("longitude"))
        assertTrue(captor.value.containsKey("limit"))
        assertTrue(captor.value.containsKey("offset"))

        assertEquals(category.alias, captor.value["categories"])
    }

    @Test(expected = MaxLimitParamException::class)
    fun `should throw an exception when max limit value for api has exceeded the maximum 50`() = runBlocking {
        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                51,
                0,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test
    fun `should include to params sort_by=rating when value for sortingstrategy is rating`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                50,
                0,
                13.123,
                123.13,
                SortingStrategy.Rating
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("sort_by"))
        assertEquals("rating", captor.value["sort_by"])
    }

    @Test
    fun `should include to params sort_by=distance when value for sortingstrategy is distance`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                50,
                0,
                13.123,
                123.13,
                SortingStrategy.Distance
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("sort_by"))
        assertEquals("distance", captor.value["sort_by"])
    }

    @Test
    fun `should not include sort_by params when sortingstrategy is default`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchBusinessesInCategoryByLocationUseCase.Params(
                category,
                50,
                0,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertFalse(captor.value.containsKey("sort_by"))
    }
}