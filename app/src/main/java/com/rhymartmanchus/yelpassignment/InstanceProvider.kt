package com.rhymartmanchus.yelpassignment

import android.content.Context
import android.util.Log
import com.rhymartmanchus.yelpassignment.BuildConfig.API_KEY
import com.rhymartmanchus.yelpassignment.coroutines.AppCoroutineDispatcher
import com.rhymartmanchus.yelpassignment.coroutines.CoroutineDispatcher
import com.rhymartmanchus.yelpassignment.data.*
import com.rhymartmanchus.yelpassignment.data.db.YelpDatabase
import com.rhymartmanchus.yelpassignment.domain.BusinessesGateway
import com.rhymartmanchus.yelpassignment.domain.CategoriesGateway
import com.rhymartmanchus.yelpassignment.domain.interactors.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

object InstanceProvider {

    var appCoroutinesDispatcher: AppCoroutineDispatcher = CoroutineDispatcher()

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor { chain ->
            val request: Request.Builder = chain.request()
                .newBuilder()
            request.addHeader("Authorization", "Bearer $API_KEY")

            Log.e("Request URL:", chain.request().url().toString())

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
            appCoroutinesDispatcher,
            categoriesGateway
        )
    }

    val fetchBusinessesByLocationUseCase: FetchBusinessesByLocationUseCase by lazy {
        FetchBusinessesByLocationUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

    val fetchBusinessesInCategoryByLocationUseCase: FetchBusinessesInCategoryByLocationUseCase by lazy {
        FetchBusinessesInCategoryByLocationUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

    val searchBusinessesByKeywordUseCase: SearchBusinessesByKeywordUseCase by lazy {
        SearchBusinessesByKeywordUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

    val searchBusinessesInCategoryByKeywordUseCase: SearchBusinessesInCategoryByKeywordUseCase by lazy {
        SearchBusinessesInCategoryByKeywordUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

    val getSubcategoryAttributedCategoriesUseCase: GetSubcategoryAttributedCategoriesUseCase by lazy {
        GetSubcategoryAttributedCategoriesUseCase(
            appCoroutinesDispatcher,
            categoriesGateway
        )
    }

    val getSubcategoryAttributedCategoriesByAliasUseCase: GetSubcategoryAttributedCategoriesByAliasUseCase by lazy {
        GetSubcategoryAttributedCategoriesByAliasUseCase(
            appCoroutinesDispatcher,
            categoriesGateway
        )
    }

    val fetchBusinessByAliasUseCase: FetchBusinessByAliasUseCase by lazy {
        FetchBusinessByAliasUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

    val fetchBusinessReviewsByAliasUseCase: FetchBusinessReviewsByAliasUseCase by lazy {
        FetchBusinessReviewsByAliasUseCase(
            appCoroutinesDispatcher,
            businessesGateway
        )
    }

}