package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import com.rhymartmanchus.yelpassignment.domain.models.Review
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class FetchBusinessReviewsByAliasUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var useCase: FetchBusinessReviewsByAliasUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = FetchBusinessReviewsByAliasUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    @Test
    fun `should fetch reviews successfully`() = runBlocking {
        `when`(gateway.fetchReviewsByAlias(ArgumentMatchers.anyString()))
            .then {
                listOf(
                    mock(Review::class.java),
                    mock(Review::class.java),
                    mock(Review::class.java)
                )
            }

        val result = useCase.execute(
            FetchBusinessReviewsByAliasUseCase.Param(
                "1234"
            )
        )

        val captor = argumentCaptor<String>()

        verify(gateway).fetchReviewsByAlias(capture(captor))

        assertEquals("1234", captor.value)
        assertEquals(3, result.reviews.size)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no reviews are retrieved`() = runBlocking {
        `when`(gateway.fetchReviewsByAlias(ArgumentMatchers.anyString()))
            .then {
                throw NoDataException("No available data")
            }


        useCase.execute(
            FetchBusinessReviewsByAliasUseCase.Param("1234")
        )

        Unit
    }

    @Test(expected = NetworkErrorException::class)
    fun `should throw an exception when network failed`() = runBlocking {
        `when`(gateway.fetchReviewsByAlias(ArgumentMatchers.anyString()))
            .then {
                throw NetworkErrorException(IOException())
            }

        useCase.execute(
            FetchBusinessReviewsByAliasUseCase.Param("1234")
        )

        Unit
    }

}