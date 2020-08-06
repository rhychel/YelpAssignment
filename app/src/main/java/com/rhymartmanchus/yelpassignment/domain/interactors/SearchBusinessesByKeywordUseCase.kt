package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.SortingStrategy
import com.rhymartmanchus.yelpassignment.domain.models.Business

class SearchBusinessesByKeywordUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: BusinessesGateway
) : UseCase<SearchBusinessesByKeywordUseCase.Response,
        SearchBusinessesByKeywordUseCase.Params>(appCoroutinesDispatcher) {

    override suspend fun run(params: Params): Response {

        if(params.keyword.isBlank() || params.keyword.isEmpty())
            throw IllegalArgumentException("`keyword` cannot be empty")

        val result = gateway.searchBusinesses(
            generateMapParams(params)
        )

        return Response(result)
    }

    private fun generateMapParams(params: Params): Map<String, String> =
        when(params.sortingStrategy) {
            SortingStrategy.Default -> mapOf(
                Pair("limit", "${params.limit}"),
                Pair("offset", "${params.offset}"),
                Pair("latitude", "${params.latitude}"),
                Pair("longitude", "${params.longitude}"),
                Pair("term", params.keyword)
            )
            SortingStrategy.Rating,
            SortingStrategy.Distance -> mapOf(
                Pair("limit", "${params.limit}"),
                Pair("offset", "${params.offset}"),
                Pair("latitude", "${params.latitude}"),
                Pair("longitude", "${params.longitude}"),
                Pair("sort_by", params.sortingStrategy.key),
                Pair("term", params.keyword)
            )
        }

    data class Params (
        val keyword: String,
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