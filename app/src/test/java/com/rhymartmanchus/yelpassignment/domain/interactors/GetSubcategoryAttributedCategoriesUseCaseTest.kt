package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.SubcategoryAttributedCategory
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class GetSubcategoryAttributedCategoriesUseCaseTest {

    @Mock
    private lateinit var gateway: CategoriesGateway

    private lateinit var useCase: GetSubcategoryAttributedCategoriesUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = GetSubcategoryAttributedCategoriesUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    @Test
    fun `should return all categories with subcategories by limit and offset`() = runBlocking {
        `when`(gateway.getSubcategoryAttributedCategory(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
            .then {
                listOf(
                    mock(SubcategoryAttributedCategory::class.java),
                    mock(SubcategoryAttributedCategory::class.java),
                    mock(SubcategoryAttributedCategory::class.java)
                )
            }

        val result = useCase.execute(
            GetSubcategoryAttributedCategoriesUseCase.Params(10, 0)
        )

        val captor = argumentCaptor<Long>()

        verify(gateway).getSubcategoryAttributedCategory(capture(captor), capture(captor))

        assertEquals(10, captor.allValues[0])
        assertEquals(0, captor.allValues[1])
        assertEquals(3, result.subcategoryAttributedCategories.size)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no data is retrieved`() = runBlocking {
        `when`(gateway.getSubcategoryAttributedCategory(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
            .then {
                throw NoDataException("No available data")
            }

        useCase.execute(GetSubcategoryAttributedCategoriesUseCase.Params(10, 10))

        Unit
    }
}