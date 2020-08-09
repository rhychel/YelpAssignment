package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Review

interface BusinessesGateway {

    suspend fun searchBusinesses(params: Map<String, String>): List<Business>
    suspend fun fetchByAlias(alias: String): Business
    suspend fun fetchReviewsByAlias(alias: String): List<Review>

}