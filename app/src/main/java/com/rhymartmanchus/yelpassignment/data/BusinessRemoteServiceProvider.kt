package com.rhymartmanchus.yelpassignment.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rhymartmanchus.yelpassignment.data.models.BusinessRaw
import com.rhymartmanchus.yelpassignment.domain.exceptions.HttpRequestException
import com.rhymartmanchus.yelpassignment.domain.models.Business
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BusinessRemoteServiceProvider (
    private val retrofit: Retrofit
) : BusinessesRemoteService {

    private fun provideRetrofitInstance(): Retrofit {
        val builder = retrofit.newBuilder()
        builder.converterFactories().clear()
        builder.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(object : TypeToken<MutableList<BusinessRaw>>(){}.type, BusinessesConverter())
                    .create()
            )
        )
        return builder.build()
    }

    override suspend fun fetchBusinesses(params: Map<String, String>): List<Business> =
        try {
            provideRetrofitInstance()
                .create(YelpEndpoint::class.java)
                .getBusinesses(params)
                .map { it.toDomain() }
        } catch (e: HttpException) {
            val jsonObject = JSONObject(e.response()?.errorBody()?.string() ?: "{}")
            val errorJsonObject = jsonObject.getJSONObject("error")

            throw HttpRequestException(
                errorJsonObject.getString("code"),
                errorJsonObject.getString("description"),
                errorJsonObject.getString("field")
            )
        }

}