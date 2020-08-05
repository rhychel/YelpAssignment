package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.data.models.BusinessRaw
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YelpEndpoint {

    @GET("/v3/businesses/search")
    suspend fun getBusinesses(@QueryMap params: Map<String, String>): List<BusinessRaw>

}