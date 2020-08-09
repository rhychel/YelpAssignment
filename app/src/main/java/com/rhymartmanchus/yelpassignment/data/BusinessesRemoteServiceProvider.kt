package com.rhymartmanchus.yelpassignment.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rhymartmanchus.yelpassignment.data.api.BusinessConverter
import com.rhymartmanchus.yelpassignment.data.api.BusinessesConverter
import com.rhymartmanchus.yelpassignment.data.api.YelpEndpoint
import com.rhymartmanchus.yelpassignment.data.api.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.data.api.safeApiCall
import com.rhymartmanchus.yelpassignment.domain.models.Business
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BusinessesRemoteServiceProvider (
    private val retrofit: Retrofit
) : BusinessesRemoteService {

    private fun provideRetrofitInstance(): Retrofit {
        val builder = retrofit.newBuilder()
        builder.converterFactories().clear()
        builder.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(object : TypeToken<MutableList<BusinessRaw>>(){}.type,
                        BusinessesConverter()
                    )
                    .registerTypeAdapter(BusinessRaw::class.java, BusinessConverter())
                    .create()
            )
        )
        return builder.build()
    }

    override suspend fun fetchBusinesses(params: Map<String, String>): List<Business> =
        safeApiCall {
            provideRetrofitInstance()
                .create(YelpEndpoint::class.java)
                .getBusinesses(params)
        }.map { it.toDomain() }

    override suspend fun fetchByAlias(alias: String): Business =
        safeApiCall {
            provideRetrofitInstance()
                .create(YelpEndpoint::class.java)
                .getBusinessByAlias(alias)
        }.toDomain()

}