package com.rhymartmanchus.yelpassignment.data.api

import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface YelpEndpoint {

    @GET("/v3/businesses/search")
    suspend fun getBusinesses(@QueryMap params: Map<String, String>): List<BusinessRaw>

    @GET("/v3/businesses/{id}")
    suspend fun getBusinessByAlias(@Path("id") alias: String): BusinessRaw

    @GET("/v3/categories")
    suspend fun getCategories(@Query("locale") locale: String): List<CategoryRaw>
}