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

class GetSubcategoryAttributedCategoriesByAliasUseCaseTest {

    @Mock
    private lateinit var gateway: CategoriesGateway

    private lateinit var useCase: GetSubcategoryAttributedCategoriesByAliasUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = GetSubcategoryAttributedCategoriesByAliasUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    @Test
    fun `should return all categories with subcategories by alias with limit and offset`() = runBlocking {
        `when`(gateway.getSubcategoryAttributedCategoryByAlias(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
            .then {
                listOf(
                    mock(SubcategoryAttributedCategory::class.java),
                    mock(SubcategoryAttributedCategory::class.java),
                    mock(SubcategoryAttributedCategory::class.java)
                )
            }

        val result = useCase.execute(
            GetSubcategoryAttributedCategoriesByAliasUseCase.Params("yelp-alias",10, 0)
        )

        val captor = argumentCaptor<Long>()
        val aliasCaptor = argumentCaptor<String>()

        verify(gateway).getSubcategoryAttributedCategoryByAlias(capture(aliasCaptor), capture(captor), capture(captor))

        assertEquals("yelp-alias", aliasCaptor.value)
        assertEquals(10, captor.allValues[0])
        assertEquals(0, captor.allValues[1])
        assertEquals(3, result.subcategoryAttributedCategories.size)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no data is retrieved`() = runBlocking {
        `when`(gateway.getSubcategoryAttributedCategoryByAlias(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
            .then {
                throw NoDataException("No available data")
            }

        useCase.execute(GetSubcategoryAttributedCategoriesByAliasUseCase.Params("yelp-alias",10, 10))

        Unit
    }
}