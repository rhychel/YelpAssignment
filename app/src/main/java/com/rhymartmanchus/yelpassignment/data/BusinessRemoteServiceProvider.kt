package com.rhymartmanchus.yelpassignment.data

import com.rhymartmanchus.yelpassignment.domain.models.Establishment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BusinessRemoteServiceProvider (
    private val retrofit: Retrofit
) : BusinessesRemoteService {

    private fun provideRetrofitInstance(): Retrofit {
        val builder = retrofit.newBuilder()
        builder.converterFactories().clear()
        builder.addConverterFactory(
            GsonConverterFactory.create()
        )
        return builder.build()
    }

    override suspend fun fetchBusinesses(params: Map<String, String>): List<Establishment> =
        provideRetrofitInstance()
            .create(YelpEndpoint::class.java)
            .getBusinesses(params)
            .map { it.toDomain() }

}