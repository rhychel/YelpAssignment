package com.rhymartmanchus.yelpassignment

import android.content.Context
import com.rhymartmanchus.yelpassignment.BuildConfig.API_KEY
import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.coroutines.CoroutineDispatcher
import com.rhymartmanchus.yelpassignment.data.*
import com.rhymartmanchus.yelpassignment.data.db.YelpDatabase
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.interactors.FetchCategoriesByLocaleUseCase
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

object InstanceProvider {

    var appCoroutineDispatcher: AppCoroutineDispatcher = CoroutineDispatcher()

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor { chain ->
            val request: Request.Builder = chain.request()
                .newBuilder()
            request.addHeader("Authorization", "Bearer $API_KEY")

            chain.proceed(request.build())
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.yelp.com")
            .client(okHttpClient)
            .build()
    }

    private lateinit var categoriesGateway: CategoriesGateway
    private lateinit var businessesGateway: BusinessesGateway

    fun initialize(appContext: Context) {

        categoriesGateway = CategoriesRepository(
            CategoriesLocalServiceProvider(
                YelpDatabase.getInstance(appContext).categoriesDao()
            ),
            CategoriesRemoteServiceProvider(
                retrofit
            )
        )

        businessesGateway = BusinessesRepository(
            BusinessesRemoteServiceProvider(
                retrofit
            )
        )

    }

    val fetchCategoriesByLocaleUseCase: FetchCategoriesByLocaleUseCase by lazy {
        FetchCategoriesByLocaleUseCase(
            appCoroutineDispatcher,
            categoriesGateway
        )
    }

}