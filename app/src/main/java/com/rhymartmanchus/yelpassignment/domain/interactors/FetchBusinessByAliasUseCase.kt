package com.rhymartmanchus.yelpassignment.domain.interactors

import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessMigratedException
import com.rhymartmanchus.yelpassignment.domain.exceptions.BusinessNotFoundException
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.models.Business

class FetchBusinessByAliasUseCase (
    appCoroutinesDispatcher: AppCoroutineDispatcher,
    private val gateway: BusinessesGateway
) : UseCase<FetchBusinessByAliasUseCase.Response, FetchBusinessByAliasUseCase.Param>(appCoroutinesDispatcher) {

    data class Param (
        val alias: String
    )

    data class Response (
        val business: Business
    )

    override suspend fun run(params: Param): Response =
        try {
            Response(
                gateway.fetchByAlias(params.alias)
            )
        } catch (e: HttpRequestException) {
            if(e.httpCode == 404)
                throw BusinessNotFoundException(params.alias)
            if(e.httpCode == 301)
                throw BusinessMigratedException()
            throw e
        }

}