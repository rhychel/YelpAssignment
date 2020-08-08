package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.any
import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.YelpLocale
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.Category
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class FetchCategoriesByLocaleUseCaseTest {

    @Mock
    private lateinit var gateway: CategoriesGateway

    private lateinit var useCase: FetchCategoriesByLocaleUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = FetchCategoriesByLocaleUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    private suspend fun mockIdealRequest() {
        `when`(gateway.fetchCategories(any()))
            .then {
                listOf(
                    mock(Category::class.java),
                    mock(Category::class.java),
                    mock(Category::class.java)
                )
            }
    }

    @Test
    fun `should fetch the all categories of a locale`() = runBlocking {
        mockIdealRequest()

        val result = useCase.execute(
            FetchCategoriesByLocaleUseCase.Param(
                Locale.getDefault()
            )
        )

        val inOrder = inOrder(gateway)
        inOrder.verify(gateway).invalidateCategories()
        inOrder.verify(gateway).fetchCategories(any())
        inOrder.verify(gateway).saveCategories(any())
        inOrder.verifyNoMoreInteractions()

        assertEquals(3, result.categories.size)
    }

    @Test
    fun `should have a valid format of locale in YelpLocale`() = runBlocking {
        mockIdealRequest()

        useCase.execute(
            FetchCategoriesByLocaleUseCase.Param(
                Locale.getDefault()
            )
        )

        val captor = argumentCaptor<YelpLocale>()

        verify(gateway).fetchCategories(capture(captor))

        assertEquals(
            generateYelpLocaleFormat(Locale.getDefault()),
            captor.value.locale
        )
    }

    private fun generateYelpLocaleFormat(locale: Locale): String =
        locale.toLanguageTag().replace("-", "_")

    @Test(expected = HttpRequestException::class)
    fun `should throw an exception when request failed`() = runBlocking {
        `when`(gateway.fetchCategories(any()))
            .then {
                throw HttpRequestException(
                    "Validation_error",
                    "error message",
                    null
                )
            }

        useCase.execute(
            FetchCategoriesByLocaleUseCase.Param(
                Locale.getDefault()
            )
        )

        Unit
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when data is not available`() = runBlocking {
        `when`(gateway.fetchCategories(any()))
            .then {
                throw NoDataException("No available data")
            }

        useCase.execute(
            FetchCategoriesByLocaleUseCase.Param(
                Locale.getDefault()
            )
        )

        Unit
    }
}