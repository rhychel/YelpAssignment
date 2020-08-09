package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.models.Review

class FetchBusinessReviewsByAliasUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: BusinessesGateway
) : UseCase<FetchBusinessReviewsByAliasUseCase.Response,
        FetchBusinessReviewsByAliasUseCase.Param>(appCoroutinesDispatcher) {

    data class Param (
        val alias: String
    )

    data class Response (
        val reviews: List<Review>
    )

    override suspend fun run(params: Param): Response =
        Response(
            gateway.fetchReviewsByAlias(params.alias)
        )

}