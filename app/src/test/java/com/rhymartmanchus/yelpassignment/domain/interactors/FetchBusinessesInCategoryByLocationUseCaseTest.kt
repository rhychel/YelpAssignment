package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.exceptions.NoDataException
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.net.SocketTimeoutException

class FetchBusinessesInCategoryByLocationUseCaseTest {

    @Before
    fun setUp() {
    }

    @Test
    fun `should fetch businesses on a category with a given coordinates`() = runBlocking {
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when no businesses found on a category in a given coordinates`() = runBlocking {
    }

    @Test(expected = SocketTimeoutException::class)
    fun `should throw an exception when nothing is responded`() = runBlocking {
    }

    @Test
    fun `params for gateway method should include location coordinates, category and limit-offset`() = runBlocking {
    }

    @Test(expected = HttpRequestException::class)
    fun `should throw an exception when max limit value for api has exceeded the maximum`() = runBlocking {
    }

    @Test
    fun `should include to params sort_by=rating when value for sortingstrategy is rating`() = runBlocking {
    }

    @Test
    fun `should include to params sort_by=distance when value for sortingstrategy is distance`() = runBlocking {
    }

    @Test
    fun `should not include sort_by params when sortingstrategy is default`() = runBlocking {
    }
}