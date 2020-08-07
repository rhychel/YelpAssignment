package com.rhymartmanchus.yelpassignment.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rhymartmanchus.yelpassignment.data.api.CategoriesConverter
import com.rhymartmanchus.yelpassignment.data.api.YelpEndpoint
import com.rhymartmanchus.yelpassignment.data.api.models.CategoryRaw
import com.rhymartmanchus.yelpassignment.data.api.safeApiCall
import com.rhymartmanchus.yelpassignment.domain.models.Category
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoriesRemoteServiceProvider (
    private val retrofit: Retrofit
) : CategoriesRemoteService {

    private fun provideRetrofitInstance(): Retrofit {
        val builder = retrofit.newBuilder()
        builder.converterFactories().clear()
        builder.addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(object : TypeToken<MutableList<CategoryRaw>>(){}.type,
                        CategoriesConverter()
                    )
                    .create()
            )
        )
        return builder.build()
    }


    override suspend fun fetchCategoriesByLocale(locale: String): List<Category> =
        safeApiCall {
            provideRetrofitInstance()
                .create(YelpEndpoint::class.java)
                .getCategories(locale)
        }.map { it.toDomain() }
}