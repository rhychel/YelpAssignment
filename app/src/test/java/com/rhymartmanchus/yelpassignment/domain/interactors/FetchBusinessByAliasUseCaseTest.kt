package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.argumentCaptor
import com.rhymartmanchus.yelpassignment.capture
import com.rhymartmanchus.yelpassignment.coroutines.TestAppCoroutinesDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessMigratedException
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessNotFoundException
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NetworkErrorException
import com.rhymartmanchus.yelpassignment.domain.models.Business
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.SocketTimeoutException

class FetchBusinessByAliasUseCaseTest {

    @Mock
    private lateinit var gateway: BusinessesGateway

    private lateinit var useCase: FetchBusinessByAliasUseCase

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        useCase = FetchBusinessByAliasUseCase(
            TestAppCoroutinesDispatcher(),
            gateway
        )

    }

    @Test
    fun `should fetch business details by alias`() = runBlocking {
        `when`(gateway.fetchByAlias(ArgumentMatchers.anyString()))
            .then {
                mock(Business::class.java)
            }

        useCase.execute(
            FetchBusinessByAliasUseCase.Param("1234")
        )

        val captor = argumentCaptor<String>()

        verify(gateway).fetchByAlias(capture(captor))

        assertEquals("1234", captor.value)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw an exception when alias is empty`() = runBlocking {

        useCase.execute(
            FetchBusinessByAliasUseCase.Param("")
        )

        Unit
    }

    @Test(expected = BusinessMigratedException::class)
    fun `should throw an exception when business is migrated`() = runBlocking {
        `when`(gateway.fetchByAlias(ArgumentMatchers.anyString()))
            .then {
                throw HttpRequestException(
                    301,"","", null
                )
            }

        useCase.execute(
            FetchBusinessByAliasUseCase.Param("1")
        )

        Unit
    }

    @Test(expected = NetworkErrorException::class)
    fun `should throw an exception when network failed`() = runBlocking {
        `when`(gateway.fetchByAlias(ArgumentMatchers.anyString()))
            .then {
                throw NetworkErrorException(SocketTimeoutException())
            }

        useCase.execute(
            FetchBusinessByAliasUseCase.Param("1")
        )

        Unit
    }

    @Test(expected = BusinessNotFoundException::class)
    fun `should throw an exception when business is not found`() = runBlocking {
        `when`(gateway.fetchByAlias(ArgumentMatchers.anyString()))
            .then {
                throw HttpRequestException(
                    404,"","", null
                )
            }

        useCase.execute(
            FetchBusinessByAliasUseCase.Param("1")
        )

        Unit
    }
}