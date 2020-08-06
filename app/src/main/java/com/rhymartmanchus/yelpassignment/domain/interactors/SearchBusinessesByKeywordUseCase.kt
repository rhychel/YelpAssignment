package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway

class SearchBusinessesByKeywordUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: BusinessesGateway
) : UseCase<SearchBusinessesByKeywordUseCase.Response, SearchBusinessesByKeywordUseCase.Params>(appCoroutinesDispatcher) {

    class Params (

    )

    class Response (

    )

    override suspend fun run(params: Params): Response {
        TODO("Not yet implemented")
    }

}