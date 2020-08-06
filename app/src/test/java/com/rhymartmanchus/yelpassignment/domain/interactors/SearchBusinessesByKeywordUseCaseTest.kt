package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.MockitoAnnotations

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

    @Test
    fun `should fetch businesses by keyword in a location successfully`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should throw an exception when no results for which relates to the keyword`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should include limit and offset to params for paging`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should include term and coordinates in params when searching for a keyword`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should include sort_by=distance when trying to sort results by distance`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should include sort_by=rating when trying to sort results by rating`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should throw an exception when the httprequest failed`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should throw an exception when ioexception happened`() {
        TODO("Not yet implemented")
    }

    @Test
    fun `should throw an exception when limit param reached the maximum value 50`() {
        TODO("Not yet implemented")
    }
}