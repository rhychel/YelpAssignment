package com.rhymartmanchus.yelpassignment.domain

import com.rhymartmanchus.yelpassignment.domain.models.Coordinates
import com.rhymartmanchus.yelpassignment.domain.models.Business

interface BusinessesGateway {

    suspend fun searchBusinesses(coordinates: Coordinates, params: Map<String, String>): List<Business>

}