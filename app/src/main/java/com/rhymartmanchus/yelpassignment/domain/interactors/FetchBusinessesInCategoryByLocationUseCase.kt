package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Category

class FetchBusinessesInCategoryByLocationUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: BusinessesGateway
) : UseCase<FetchBusinessesInCategoryByLocationUseCase.Response,
        FetchBusinessesInCategoryByLocationUseCase.Params>(appCoroutinesDispatcher) {

    override suspend fun run(params: Params): Response =
        Response(
            gateway.searchBusinesses(generateMapParams(params))
        )

    private fun generateMapParams(params: Params): Map<String, String> =
        when(params.sortingStrategy) {
            SortingStrategy.Default -> mapOf(
                Pair("categories", params.category.alias),
                Pair("limit", "${params.limit}"),
                Pair("offset", "${params.offset}"),
                Pair("latitude", "${params.latitude}"),
                Pair("longitude", "${params.longitude}")
            )
            SortingStrategy.Rating,
            SortingStrategy.Distance -> mapOf(
                Pair("categories", params.category.alias),
                Pair("limit", "${params.limit}"),
                Pair("offset", "${params.offset}"),
                Pair("latitude", "${params.latitude}"),
                Pair("longitude", "${params.longitude}"),
                Pair("sort_by", params.sortingStrategy.key)
            )
        }

    data class Params (
        val category: Category,
        val limit: Int,
        val offset: Int,
        val latitude: Double,
        val longitude: Double,
        val sortingStrategy: SortingStrategy
    )

    data class Response (
        val businesses: List<Business>
    )

}