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
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException
import kotlin.math.exp

class SearchBusinessesInCategoryByKeywordUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var category: Category

    private lateinit var useCase: SearchBusinessesInCategoryByKeywordUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        category = Category(
            "my-category",
            "My Category",
            listOf(
                "1-1"
            )
        )

        useCase = SearchBusinessesInCategoryByKeywordUseCase(
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
    fun `should fetch businesses in category by keyword in a location successfully`() = runBlocking {
        mockIdealRequest()

        val result = useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
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

    @Test
    fun `params gateway should include keyword, category, terms, coordinates, limit and offset`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
                category,
                1,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertTrue(captor.value.containsKey("term"))
        assertTrue(captor.value.containsKey("categories"))
        assertTrue(captor.value.containsKey("limit"))
        assertTrue(captor.value.containsKey("offset"))
        assertTrue(captor.value.containsKey("latitude"))
        assertTrue(captor.value.containsKey("longitude"))

        assertEquals("my-keyword", captor.value["term"])
        assertEquals(category.alias, captor.value["categories"])
    }

    @Test
    fun `should include sort_by=distance when trying to sort results by distance`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
                category,
                1,
                1,
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
    fun `should include sort_by=rating when trying to sort results by rating`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
                category,
                1,
                1,
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
    fun `should not include sort_by param when sortingStrategy is Default`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
                category,
                1,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        val captor = argumentCaptor<Map<String, String>>()

        verify(gateway).searchBusinesses(capture(captor))

        assertFalse(captor.value.containsKey("sort_by"))
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no results for which relates to the keyword`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NoDataException("No businesses found")
            }

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
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

    @Test(expected = HttpRequestException::class)
    fun `should throw an exception when the httprequest failed`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw HttpRequestException(
                    "VALIDATION_ERROR",
                    "error message",
                    null
                )
            }

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
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
    fun `should throw an exception when network error happened`() = runBlocking {
        `when`(gateway.searchBusinesses(ArgumentMatchers.anyMap()))
            .then {
                throw NetworkErrorException(IOException())
            }

        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
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

    @Test(expected = MaxLimitParamException::class)
    fun `should throw an exception when limit param reached the maximum value 50`() = runBlocking {
        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "my-keyword",
                category,
                51,
                1,
                13.123,
                123.13,
                SortingStrategy.Default
            )
        )

        Unit
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw an exception when keyword is empty`() = runBlocking {
        useCase.execute(
            SearchBusinessesInCategoryByKeywordUseCase.Params(
                "",
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
}