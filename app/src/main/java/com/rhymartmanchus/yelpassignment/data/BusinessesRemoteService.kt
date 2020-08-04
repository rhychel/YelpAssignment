package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Establishment

interface BusinessesRemoteService {

    suspend fun fetchBusinesses(params: Map<String, String>): List<Establishment>

}