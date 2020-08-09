package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.models.Business

class BusinessesRepository (
    private val remote: BusinessesRemoteService
) : BusinessesGateway {

    override suspend fun searchBusinesses(params: Map<String, String>): List<Business> =
        remote.fetchBusinesses(params)

    override suspend fun fetchByAlias(alias: String): Business {
        TODO("Not yet implemented")
    }

}