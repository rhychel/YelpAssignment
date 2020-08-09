package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Business

interface BusinessesRemoteService {

    suspend fun fetchBusinesses(params: Map<String, String>): List<Business>
    suspend fun fetchByAlias(alias: String): Business

}