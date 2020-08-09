package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Business
import com.rhymartmanchus.yelpassignment.domain.models.Review

interface BusinessesRemoteService {

    suspend fun fetchBusinesses(params: Map<String, String>): List<Business>
    suspend fun fetchByAlias(alias: String): Business
    suspend fun fetchReviewsByAlias(alias: String): List<Review>

}