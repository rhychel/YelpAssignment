package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Business

interface BusinessesGateway {

    suspend fun searchBusinesses(params: Map<String, String>): List<Business>
    suspend fun fetchByAlias(alias: String): Business

}